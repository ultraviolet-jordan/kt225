package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.InvalidUsernameOrPasswordResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class InvalidUsernameOrPasswordLoginEncoder : CodecEncoder<InvalidUsernameOrPasswordResponse> {
    override suspend fun encode(session: Session, message: InvalidUsernameOrPasswordResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(3)
        buffer.flush()
        session.close()
    }
}
