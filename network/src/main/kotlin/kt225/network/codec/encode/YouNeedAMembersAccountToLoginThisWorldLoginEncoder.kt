package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.YouNeedAMembersAccountToLoginThisWorldLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class YouNeedAMembersAccountToLoginThisWorldLoginEncoder : CodecEncoder<YouNeedAMembersAccountToLoginThisWorldLoginResponse> {
    override suspend fun encode(session: Session, message: YouNeedAMembersAccountToLoginThisWorldLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(12)
        buffer.flush()
        session.close()
    }
}
