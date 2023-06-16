package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.ThisServerIsBeingUpdatedLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class ThisServerIsBeingUpdatedLoginEncoder : CodecEncoder<ThisServerIsBeingUpdatedLoginResponse> {
    override suspend fun encode(session: Session, message: ThisServerIsBeingUpdatedLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(14)
        buffer.flush()
        session.close()
    }
}
