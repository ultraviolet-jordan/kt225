package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.LoginAttemptsExceededLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class LoginAttemptsExceededLoginEncoder : CodecEncoder<LoginAttemptsExceededLoginResponse> {
    override suspend fun encode(session: Session, message: LoginAttemptsExceededLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(16)
        buffer.flush()
        session.close()
    }
}
