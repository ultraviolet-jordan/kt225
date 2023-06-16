package kt225.common.network

import io.ktor.utils.io.ByteReadChannel

/**
 * @author Jordan Abraham
 */
interface CodecDecoder {
    suspend fun decode(session: Session, channel: ByteReadChannel)
}
