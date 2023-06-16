package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.LoginLimitExceededLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class LoginLimitExceededLoginEncoder : CodecEncoder<LoginLimitExceededLoginResponse> {
    override suspend fun encode(session: Session, message: LoginLimitExceededLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(9)
        buffer.flush()
        session.close()
    }
}
