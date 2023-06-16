package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.BadSessionLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class BadSessionLoginEncoder : CodecEncoder<BadSessionLoginResponse> {
    override suspend fun encode(session: Session, message: BadSessionLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(10)
        buffer.flush()
        session.close()
    }
}
