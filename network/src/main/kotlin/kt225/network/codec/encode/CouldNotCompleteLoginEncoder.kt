package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.CouldNotCompleteLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class CouldNotCompleteLoginEncoder : CodecEncoder<CouldNotCompleteLoginResponse> {
    override suspend fun encode(session: Session, message: CouldNotCompleteLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(13)
        buffer.flush()
        session.close()
    }
}
