package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.SuccessfulModeratorLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class SuccessfulModeratorLoginEncoder : CodecEncoder<SuccessfulModeratorLoginResponse> {
    override suspend fun encode(session: Session, message: SuccessfulModeratorLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(18)
    }
}
