package kt225.network.codec.decode

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.utils.io.ByteReadChannel
import kt225.common.buffer.g1
import kt225.common.buffer.g4
import kt225.common.buffer.gstr
import kt225.common.buffer.rsadec
import kt225.common.network.CodecDecoder
import kt225.common.network.Session
import kt225.network.codec.encode.LoginEncoder
import kt225.network.codec.type.LoginResponse
import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class LoginDecoder @Inject constructor(
    environment: ApplicationEnvironment
) : CodecDecoder {
    private val exponent = environment.config.property("game.rsa.exponent").getString()
    private val modulus = environment.config.property("game.rsa.modulus").getString()

    override suspend fun decode(session: Session, channel: ByteReadChannel) {
        val loginType = channel.readByte().toInt() and 0xff
        val length = channel.readByte().toInt() and 0xff
        val buffer = ByteBuffer.allocate(length)
        val bytes = channel.readFully(buffer).also {
            buffer.flip()
        }
        if (bytes != length) {
            session.close()
            return
        }

        val version = buffer.g1
        val properties = buffer.g1
        val crcs = IntArray(9) { buffer.g4() }
        buffer.rsadec(BigInteger(exponent), BigInteger(modulus))
        val rsaTen = buffer.g1
        val clientSeed = IntArray(4) { buffer.g4() }
        val serverSeed = IntArray(clientSeed.size) { clientSeed[it] + 50 }
        val uid = buffer.g4()
        val username = buffer.gstr()
        val password = buffer.gstr()

        session.codec(
            type = LoginEncoder::class,
            message = LoginResponse(loginType, version, properties, crcs, rsaTen, uid, username, password, clientSeed, serverSeed)
        )
    }
}
