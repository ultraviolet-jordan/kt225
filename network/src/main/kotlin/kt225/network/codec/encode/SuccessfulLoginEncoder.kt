package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.SuccessfulLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class SuccessfulLoginEncoder : CodecEncoder<SuccessfulLoginResponse> {
    override suspend fun encode(session: Session, message: SuccessfulLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(2)
    }
}
