package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.decode.LoginDecoder
import kt225.network.codec.type.ServerSeedResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class ServerSeedEncoder : CodecEncoder<ServerSeedResponse> {
    override suspend fun encode(session: Session, message: ServerSeedResponse, buffer: ByteWriteChannel) {
        buffer.writeLong(message.seed)
        buffer.flush()
        session.codec(LoginDecoder::class)
    }
}
