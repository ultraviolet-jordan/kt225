package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.ThisWorldIsFullLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class ThisWorldIsFullLoginEncoder : CodecEncoder<ThisWorldIsFullLoginResponse> {
    override suspend fun encode(session: Session, message: ThisWorldIsFullLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(7)
        buffer.flush()
        session.close()
    }
}
