package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.SuccessfulReconnectLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class SuccessfulReconnectLoginEncoder : CodecEncoder<SuccessfulReconnectLoginResponse> {
    override suspend fun encode(session: Session, message: SuccessfulReconnectLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(15)
    }
}
