package kt225.network.codec.type

import kt225.common.network.CodecEncoderType

/**
 * @author Jordan Abraham
 */
data class LoginResponse(
    val loginType: Int,
    val version: Int,
    val properties: Int,
    val crcs: IntArray,
    val rsaTen: Int,
    val uid: Int,
    val username: String,
    val password: String,
    val clientSeed: IntArray,
    val serverSeed: IntArray,
) : CodecEncoderType
