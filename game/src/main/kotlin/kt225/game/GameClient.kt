package kt225.game

import com.runetopic.cryptography.isaac.ISAAC
import com.runetopic.cryptography.toISAAC
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.util.logging.Logger
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.time.withTimeout
import kt225.common.buffer.RSByteBuffer
import kt225.common.game.Client
import kt225.common.game.world.World
import kt225.common.packet.Packet
import kt225.common.packet.PacketGroup
import kt225.game.entity.player.EntityPlayer
import java.math.BigInteger
import java.nio.ByteBuffer
import java.time.Duration
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
class GameClient(
    private val logger: Logger,
    private val socket: Socket,
    private val crcs: IntArray,
    environment: ApplicationEnvironment,
    private val world: World
) : Client {
    private val readChannel = socket.openReadChannel()
    private val writeChannel = socket.openWriteChannel()

    private val readChannelQueue = ConcurrentHashMap<Int, ArrayBlockingQueue<PacketGroup>>()
    private val writeChannelQueue = RSByteBuffer(ByteBuffer.allocateDirect(40_000))

    private val exponent = environment.config.property("game.rsa.exponent").getString()
    private val modulus = environment.config.property("game.rsa.modulus").getString()

    private val seed: Long = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
    private var clientIsaac: ISAAC? = null
    private var serverIsaac: ISAAC? = null

    override suspend fun acceptHandshake() {
        try {
            writeChannel.writeLong(seed)
            writeChannel.flush()

            val loginAttemptType = readChannel.readByte().toInt() and 0xFF
            if (loginAttemptType != RequestOpcodeId.NEW_LOGIN_ATTEMPT && loginAttemptType != RequestOpcodeId.RECONNECTING_LOGIN_ATTEMPT) {
                writeLoginResponse(ResponseOpcodeId.BAD_SESSION_OPCODE, true)
                close()
                return
            }

            val payloadLength = readChannel.readByte().toInt() and 0xFF
            val payload = ByteBuffer.allocate(payloadLength)
            val readBytes = readChannel.readFully(payload)
            payload.flip()
            if (readBytes != payloadLength) {
                writeLoginResponse(ResponseOpcodeId.BAD_SESSION_OPCODE, true)
                close()
                return
            }

            acceptLogin(RSByteBuffer(payload))
        } catch (exception: Exception) {
            close()
            logger.error(exception.stackTraceToString())
        }
    }

    override suspend fun acceptLogin(buffer: RSByteBuffer) {
        val version = buffer.g1()
        if (version != 225) {
            writeLoginResponse(ResponseOpcodeId.CLIENT_OUTDATED_OPCODE, true)
            close()
            return
        }

        val properties = buffer.g1()

        val loginCrcs = IntArray(9) { buffer.g4() }
        if (!loginCrcs.contentEquals(crcs)) {
            writeLoginResponse(ResponseOpcodeId.CLIENT_OUTDATED_OPCODE, true)
            close()
            return
        }

        val rsa = RSByteBuffer(buffer.rsaDecrypt(BigInteger(exponent), BigInteger(modulus)))

        if (rsa.g1() != 10) {
            writeLoginResponse(ResponseOpcodeId.CLIENT_OUTDATED_OPCODE, true)
            close()
            return
        }

        val clientSeed = IntArray(4) { rsa.g4() }
        val serverSeed = IntArray(clientSeed.size) { clientSeed[it] + 50 }
        val uid = rsa.g4()
        val username = rsa.gstr()
        val password = rsa.gstr()

        logger.info("$uid")
        logger.info(username)
        logger.info(password)

        writeLoginResponse(ResponseOpcodeId.LOGIN_SUCCESS_OPCODE, false)

        val player = EntityPlayer(this, world)

        world.requestLogin(player)

        this.clientIsaac = clientSeed.toISAAC()
        this.serverIsaac = serverSeed.toISAAC()

        while (true) {
            val packet = withTimeout(Duration.ofSeconds(30)) { awaitPacket() } ?: continue
        }
    }

    override suspend fun writeLoginResponse(responseId: Int, flush: Boolean) {
        logger.info("Write login response: ResponseId=$responseId, Flushed=$flush")
        writeChannel.writeByte(responseId.toByte())
        if (flush) {
            flushWriteChannel()
        }
    }

    override suspend fun awaitPacket(): Packet? {
        val id = readPacketId()
        if (id < 0 || id > PacketLengths.lengths.size) {
            readChannel.discard(readChannel.availableForRead.toLong())
            return null
        }
        val size = readPacketSize(PacketLengths.lengths[id])
        val buffer = RSByteBuffer(readChannel.readPacket(size).readBytes(size))

        logger.info("Incoming Packet: Id=$id, Size=$size")
        return null
    }

    override fun processReadPool() {
        TODO("Not yet implemented")
    }

    override fun writePacket(packet: Packet) {
        TODO("Not yet implemented")
    }

    override fun processWritePool() {
        TODO("Not yet implemented")
    }

    override suspend fun readPacketId(): Int = ((readChannel.readByte().toInt() and 0xFF) - clientIsaac!!.getNext() and 0xff)

    override suspend fun readPacketSize(length: Int): Int {
        if (length != -1 && length != -2) {
            return length
        }
        return if (length == -1) (readChannel.readByte().toInt() and 0xFF) else (readChannel.readShort().toInt() and 0xFFFF)
    }

    override fun flushWriteChannel() {
        writeChannel.flush()
    }

    override fun close() {
        socket.close()
    }

    object RequestOpcodeId {
        const val NEW_LOGIN_ATTEMPT = 16
        const val RECONNECTING_LOGIN_ATTEMPT = 18
    }

    object ResponseOpcodeId {
        const val LOGIN_SUCCESS_OPCODE = 2
        const val CLIENT_OUTDATED_OPCODE = 6
        const val BAD_SESSION_OPCODE = 10
    }

    object PacketLengths {
        val lengths = intArrayOf(
            0, 0, 2, 0, -1, 0, 6, 4, 2, 2,
            0, 8, 0, 0, 0, 0, 0, 1, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 2, 0, 0,
            3, 6, 0, 0, 0, 0, 0, 0, 6, 0,
            6, 0, 0, 0, 0, 0, 0, 0, 8, 0,
            0, 0, 13, 2, 0, 0, 0, 0, 0, 6,
            0, 0, 0, 0, 0, 0, 4, 0, 0, 0,
            0, 6, 0, 0, 0, 6, 0, 0, 0, 8,
            0, -1, 0, 0, 0, 0, 0, 0, 4, 0,
            0, 0, 0, -1, 0, 0, 6, 6, 0, 0,
            2, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 2, 0, 0, 6, 0, 8, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            12, 0, 0, 6, 4, 0, 0, 0, 8, 0,
            6, 0, 0, 0, 0, 0, -1, 0, 9, 0,
            3, 0, 0, 0, 0, 2, 0, 6, 3, 6,
            0, 0, 0, 0, 2, -1, 0, 0, 0, 0,
            0, 8, 6, 0, 0, 1, 2, 4, 6, 0,
            0, -1, 0, 0, 0, 2, 0, 0, 0, 6,
            10, 0, 0, 0, 2, 6, 0, 0, 0, 0,
            6, 0, 8, 0, 0, 0, 2, 0, 0, 0,
            0, 6, 6, 0, 0, 3, 0, 0, 0, -1,
            6, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 1, 0, 4, 4, 4, 1, 12,
            0, 0, 0, 0, 3, 6, 0, 6, 8, 0,
            0, 0, 0, 0, 0, 0
        )
    }
}
