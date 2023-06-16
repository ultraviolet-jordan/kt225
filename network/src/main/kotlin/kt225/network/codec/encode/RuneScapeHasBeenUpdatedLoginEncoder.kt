package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.RuneScapeHasBeenUpdatedLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class RuneScapeHasBeenUpdatedLoginEncoder : CodecEncoder<RuneScapeHasBeenUpdatedLoginResponse> {
    override suspend fun encode(session: Session, message: RuneScapeHasBeenUpdatedLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(6)
        buffer.flush()
        session.close()
    }
}
