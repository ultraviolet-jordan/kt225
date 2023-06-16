package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.LoginServerOfflineLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class LoginServerOfflineLoginEncoder : CodecEncoder<LoginServerOfflineLoginResponse> {
    override suspend fun encode(session: Session, message: LoginServerOfflineLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(8)
        buffer.flush()
        session.close()
    }
}
