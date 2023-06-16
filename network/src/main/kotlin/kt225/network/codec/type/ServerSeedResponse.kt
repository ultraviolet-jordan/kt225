package kt225.network.codec.type

import kt225.common.network.CodecEncoderType

/**
 * @author Jordan Abraham
 */
data class ServerSeedResponse(
    val seed: Long
) : CodecEncoderType
