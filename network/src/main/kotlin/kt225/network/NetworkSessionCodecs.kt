package kt225.network

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.common.network.CodecDecoder
import kt225.common.network.CodecEncoder
import kt225.common.network.CodecEncoderType

/**
 * @author Jordan Abraham
 */
@Singleton
data class NetworkSessionCodecs @Inject constructor(
    val encoders: Set<CodecEncoder<CodecEncoderType>>,
    val decoders: Set<CodecDecoder>
)
