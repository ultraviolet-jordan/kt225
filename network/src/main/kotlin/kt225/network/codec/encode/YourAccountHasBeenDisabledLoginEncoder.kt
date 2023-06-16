package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.YourAccountHasBeenDisabledLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class YourAccountHasBeenDisabledLoginEncoder : CodecEncoder<YourAccountHasBeenDisabledLoginResponse> {
    override suspend fun encode(session: Session, message: YourAccountHasBeenDisabledLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(4)
        buffer.flush()
        session.close()
    }
}
