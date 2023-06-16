package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.LoginServerRejectedSessionLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class LoginServerRejectedSessionLoginEncoder : CodecEncoder<LoginServerRejectedSessionLoginResponse> {
    override suspend fun encode(session: Session, message: LoginServerRejectedSessionLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(11)
        buffer.flush()
        session.close()
    }
}
