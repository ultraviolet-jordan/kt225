package kt225.game

import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.util.logging.Logger
import kt225.common.buffer.RSByteBuffer
import kt225.common.game.Client
import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class GameClient(
    private val logger: Logger,
    private val socket: Socket,
    private val crcs: IntArray,
    private val environment: ApplicationEnvironment
) : Client {
    private val readChannel = socket.openReadChannel()
    private val writeChannel = socket.openWriteChannel()
    private val seed: Long = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()

    private val exponent = environment.config.property("game.rsa.exponent").getString()
    private val modulus = environment.config.property("game.rsa.modulus").getString()

    override suspend fun accept() {
        try {
            writeChannel.writeLong(seed)
            writeChannel.flush()

            val loginAttemptType = readChannel.readByte().toInt() and 0xFF
            if (loginAttemptType != RequestOpcode.NEW_LOGIN_ATTEMPT && loginAttemptType != RequestOpcode.RECONNECTING_LOGIN_ATTEMPT) {
                writeLoginResponse(ResponseOpcode.BAD_SESSION_OPCODE)
                close()
                return
            }

            val payloadLength = readChannel.readByte().toInt() and 0xFF
            val payload = ByteBuffer.allocate(payloadLength)
            val readBytes = readChannel.readFully(payload)
            payload.flip()
            if (readBytes != payloadLength) {
                writeLoginResponse(ResponseOpcode.BAD_SESSION_OPCODE)
                close()
                return
            }

            readLogin(RSByteBuffer(payload))
        } catch (exception: Exception) {
            close()
            logger.error(exception.stackTraceToString())
        }
    }

    override suspend fun readLogin(buffer: RSByteBuffer) {
        val version = buffer.g1()
        if (version != 225) {
            writeLoginResponse(ResponseOpcode.CLIENT_OUTDATED_OPCODE)
            close()
            return
        }

        val properties = buffer.g1()

        val loginCrcs = IntArray(9) { buffer.g4() }
        if (!loginCrcs.contentEquals(crcs)) {
            writeLoginResponse(ResponseOpcode.CLIENT_OUTDATED_OPCODE)
            close()
            return
        }

        val rsa = RSByteBuffer(buffer.rsaDecrypt(BigInteger(exponent), BigInteger(modulus)))

        if (rsa.g1() != 10) {
            writeLoginResponse(ResponseOpcode.CLIENT_OUTDATED_OPCODE)
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

        writeLogin()
    }

    override suspend fun writeLogin() {
        TODO("Not yet implemented")
    }

    override fun close() {
        socket.close()
    }

    private suspend fun writeLoginResponse(opcode: Int) {
        writeChannel.writeByte(opcode.toByte())
        writeChannel.flush()
    }

    private object RequestOpcode {
        const val NEW_LOGIN_ATTEMPT = 16
        const val RECONNECTING_LOGIN_ATTEMPT = 18
    }

    private object ResponseOpcode {
        const val LOGIN_SUCCESS_OPCODE = 2
        const val CLIENT_OUTDATED_OPCODE = 6
        const val BAD_SESSION_OPCODE = 10
    }
}
