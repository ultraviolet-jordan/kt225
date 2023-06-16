package kt225.common.network

import io.ktor.utils.io.ByteWriteChannel

/**
 * @author Jordan Abraham
 */
interface CodecEncoder<out T : CodecEncoderType> {
    suspend fun encode(session: Session, message: @UnsafeVariance T, buffer: ByteWriteChannel)
}
