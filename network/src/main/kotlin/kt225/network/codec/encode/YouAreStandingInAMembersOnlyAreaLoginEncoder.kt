package kt225.network.codec.encode

import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.network.CodecEncoder
import kt225.common.network.Session
import kt225.network.codec.type.YouAreStandingInAMembersOnlyAreaLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class YouAreStandingInAMembersOnlyAreaLoginEncoder : CodecEncoder<YouAreStandingInAMembersOnlyAreaLoginResponse> {
    override suspend fun encode(session: Session, message: YouAreStandingInAMembersOnlyAreaLoginResponse, buffer: ByteWriteChannel) {
        buffer.writeByte(5)
        buffer.flush()
        session.close()
    }
}
