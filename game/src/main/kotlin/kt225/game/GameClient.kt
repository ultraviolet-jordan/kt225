package kt225.game

import com.runetopic.cryptography.isaac.ISAAC
import com.runetopic.cryptography.toISAAC
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.util.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.time.withTimeout
import kt225.common.buffer.g1
import kt225.common.buffer.g4
import kt225.common.buffer.gstr
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.rsaDecrypt
import kt225.common.game.Client
import kt225.common.game.world.World
import kt225.common.packet.Packet
import kt225.common.packet.PacketBuilder
import kt225.common.packet.PacketGroup
import kt225.common.packet.PacketHandler
import kt225.common.packet.PacketReader
import kt225.game.GameClient.LoginRequestId.NEW_LOGIN_ATTEMPT
import kt225.game.GameClient.LoginRequestId.RECONNECTING_LOGIN_ATTEMPT
import kt225.game.GameClient.LoginResponseId.BAD_SESSION_ID
import kt225.game.GameClient.LoginResponseId.COULD_NOT_COMPLETE_LOGIN
import kt225.game.GameClient.LoginResponseId.LOGIN_SUCCESS
import kt225.game.GameClient.LoginResponseId.RUNESCAPE_HAS_BEEN_UPDATED
import kt225.game.entity.player.EntityPlayer
import java.math.BigInteger
import java.nio.ByteBuffer
import java.time.Duration
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
class GameClient(
    private val logger: Logger,
    private val socket: Socket,
    private val crcs: IntArray,
    environment: ApplicationEnvironment,
    private val world: World,
    private val packetBuilders: Map<KClass<*>, PacketBuilder<Packet>>,
    private val packetReaders: Map<Int, PacketReader<Packet>>,
    private val packetHandlers: Map<KClass<*>, PacketHandler<Packet>>
) : Client {
    private val readChannel = socket.openReadChannel()
    private val writeChannel = socket.openWriteChannel()

    private val readChannelQueue = ConcurrentHashMap<Int, ArrayBlockingQueue<PacketGroup>>()
    private val writeChannelQueue = ByteBuffer.allocateDirect(100_000)

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
            if (loginAttemptType != NEW_LOGIN_ATTEMPT && loginAttemptType != RECONNECTING_LOGIN_ATTEMPT) {
                writeLoginResponse(COULD_NOT_COMPLETE_LOGIN, true)
                close()
                return
            }

            val length = readChannel.readByte().toInt() and 0xFF
            val buffer = ByteBuffer.allocate(length)
            val readBytes = readChannel.readFully(buffer)
            if (readBytes != length) {
                writeLoginResponse(COULD_NOT_COMPLETE_LOGIN, true)
                close()
                return
            }

            acceptLogin(buffer.flip())
        } catch (exception: Exception) {
            close()
            logger.error(exception.stackTraceToString())
        }
    }

    override suspend fun acceptLogin(buffer: ByteBuffer) {
        val version = buffer.g1()
        if (version != 225) {
            writeLoginResponse(RUNESCAPE_HAS_BEEN_UPDATED, true)
            close()
            return
        }

        val properties = buffer.g1()

        val loginCrcs = IntArray(9) { buffer.g4() }
        if (!loginCrcs.contentEquals(crcs)) {
            writeLoginResponse(RUNESCAPE_HAS_BEEN_UPDATED, true)
            close()
            return
        }

        val rsa = ByteBuffer.wrap(buffer.rsaDecrypt(BigInteger(exponent), BigInteger(modulus)))

        if (rsa.g1() != 10) {
            writeLoginResponse(BAD_SESSION_ID, true)
            close()
            return
        }

        val clientSeed = IntArray(4) { rsa.g4() }
        val serverSeed = IntArray(clientSeed.size) { clientSeed[it] + 50 }
        val uid = rsa.g4()
        val username = rsa.gstr()
        val password = rsa.gstr()

        this.clientIsaac = clientSeed.toISAAC()
        this.serverIsaac = serverSeed.toISAAC()

        writeLoginResponse(LOGIN_SUCCESS, false)
        acceptGame(properties, clientSeed, serverSeed, uid, username, password)
    }

    override suspend fun acceptGame(properties: Int, clientSeed: IntArray, serverSeed: IntArray, uid: Int, username: String, password: String) {
        val player = EntityPlayer(this, world)
        world.requestLogin(player)

        while (true) {
            val packet = withTimeout(Duration.ofSeconds(30)) { awaitPacket() } ?: continue
            val handler = packetHandlers[packet::class] ?: continue
            handler.handlePacket(packet, player)
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
        val id = ((readChannel.readByte().toInt() and 0xFF) - clientIsaac!!.getNext() and 0xFF)
        if (id > PacketLengths.lengths.size) {
            readChannel.discard(readChannel.availableForRead.toLong())
            return null
        }
        val serverLength = PacketLengths.lengths[id]
        val clientLength = when {
            serverLength != -1 && serverLength != -2 -> serverLength
            serverLength == -1 -> (readChannel.readByte().toInt() and 0xFF)
            else -> (readChannel.readShort().toInt() and 0xFFFF)
        }

        val reader = packetReaders[id]
        if (reader == null) {
            logger.info("Reader not found for packet with id: $id")
            readChannel.discard(clientLength.toLong())
            return null
        }

        if (reader.length != serverLength) {
            logger.debug("Packet reader length and server length mismatch. Packet Reader length is ${reader.length} and server length is $serverLength.")
            readChannel.discard(clientLength.toLong())
            return null
        }

        if (reader.length != -1 && reader.length != clientLength) {
            logger.debug("Packet length mismatch. Packet Reader length is ${reader.length} and client length was $clientLength.")
            // Discard the bytes from the read channel.
            readChannel.discard(clientLength.toLong())
            return null
        }

        val buffer = ByteBuffer.allocate(clientLength)
        val readBytes = readChannel.readFully(buffer)
        if (readBytes != clientLength) {
            logger.info("Packet buffer read bytes mismatch. Read bytes was $readBytes and payload length was $clientLength")
            readChannel.discard(readChannel.availableForRead.toLong())
            return null
        }
        val packet = reader.readPacket(buffer, clientLength) ?: return null

        logger.info("Incoming Packet: Id=$id, ServerLength=$serverLength, ClientLength=$clientLength")
        return packet
    }

    override fun flushReadQueue() {
        TODO("Not yet implemented")
    }

    override fun writePacket(packet: Packet) {
        try {
            val builder = packetBuilders[packet::class] ?: return

            writeChannelQueue.p1(builder.id + serverIsaac!!.getNext() and 0xFF)

            if (builder.length != -1 && builder.length != -2) {
                builder.buildPacket(packet, writeChannelQueue)
                return
            }

            val startPos = writeChannelQueue.position()
            val offset = startPos + if (builder.length == -1) 1 else 2
            writeChannelQueue.position(offset)
            builder.buildPacket(packet, writeChannelQueue)
            val endPos = writeChannelQueue.position()
            val size = endPos - offset
            writeChannelQueue.position(startPos)
            if (builder.length == -1) {
                writeChannelQueue.p1(size)
            } else {
                writeChannelQueue.p2(size)
            }
            writeChannelQueue.position(endPos)
            logger.info("Write packet: Id=${builder.id}, Size=$size")
        } catch (exception: Exception) {
            close()
            logger.error(exception.stackTraceToString())
        }
    }

    override fun flushWriteQueue() {
        if (writeChannel.isClosedForWrite) return
        // This way we only have to suspend once per client.
        runBlocking(Dispatchers.IO) {
            writeChannel.writeFully(writeChannelQueue.flip())
        }
        writeChannel.flush()
        writeChannelQueue.clear()
    }

    override fun flushWriteChannel() {
        writeChannel.flush()
    }

    override fun close() {
        socket.close()
    }

    object LoginRequestId {
        const val NEW_LOGIN_ATTEMPT = 16
        const val RECONNECTING_LOGIN_ATTEMPT = 18
    }

    object LoginResponseId {
        const val LOGIN_SUCCESS = 2
        const val INVALID_USERNAME_OR_PASSWORD = 3
        const val YOUR_ACCOUNT_HAS_BEEN_DISABLED = 4
        const val YOUR_ACCOUNT_IS_ALREADY_LOGGED_IN = 5
        const val RUNESCAPE_HAS_BEEN_UPDATED = 6
        const val THIS_WORLD_IS_FULL = 7
        const val LOGIN_SERVER_OFFLINE = 8
        const val LOGIN_LIMIT_EXCEEDED = 9
        const val BAD_SESSION_ID = 10
        const val LOGIN_SERVER_REJECTED_SESSION = 11
        const val YOU_NEED_A_MEMBERS_ACCOUNT_TO_LOGIN_THIS_WORLD = 12
        const val COULD_NOT_COMPLETE_LOGIN = 13
        const val THE_SERVER_IS_BEING_UPDATED = 14
        const val LOGIN_ATTEMPTS_EXCEEDED = 16
        const val YOU_ARE_STANDING_IN_A_MEMBERS_ONLY_AREA = 17
        const val LOGIN_SUCCESS_MODERATOR = 18
    }

    object PacketLengths {
        val lengths = intArrayOf(
            0, 0, 2, 0, -1, 0, 6, 4, 2, 2, // 0
            0, 8, 0, 0, 0, 0, 0, 1, 0, 0, // 10
            0, 0, 0, 0, 0, 0, 0, 2, 0, 0, // 20
            3, 6, 0, 0, 0, 0, 0, 0, 6, 0, // 30
            6, 0, 0, 0, 0, 0, 0, 0, 8, 0, // 40
            0, 0, 13, 2, 0, 0, 0, 0, 0, 6, // 50
            0, 0, 0, 0, 0, 0, 4, 0, 0, 0, // 60
            0, 6, 0, 0, 0, 6, 0, 0, 0, 8, // 70
            0, -1, 0, 0, 0, 0, 0, 0, 4, 0, // 80
            0, 0, 0, -1, 0, 0, 6, 6, 0, 0, // 90
            2, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 100
            0, 0, 0, 2, 0, 0, 6, 0, 8, 0, // 110
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 120
            12, 0, 0, 6, 4, 0, 0, 0, 8, 0, // 130
            6, 0, 0, 0, 0, 0, -1, 0, 9, 0, // 140
            -1, 0, 0, 0, 0, 2, 0, 6, 3, 6, // 150
            0, 0, 0, 0, 2, -1, 0, 0, 0, 0, // 160
            0, 8, 6, 0, 0, 1, 2, 4, 6, 0, // 170
            0, -1, 0, 0, 0, 2, 0, 0, 0, 6, // 180
            10, 0, 0, 0, 2, 6, 0, 0, 0, 0, // 190
            6, 0, 8, 0, 0, 0, 2, 0, 0, 0, // 200
            0, 6, 6, 0, 0, 3, 0, 0, 0, -1, // 210
            6, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 220
            0, 0, 0, 1, 0, 4, 4, 4, 1, 12, // 230
            0, 0, 0, 0, 3, 6, 0, 6, 8, 0, // 240
            0, 0, 0, 0, 0, 0 // 250
        )
    }
}
