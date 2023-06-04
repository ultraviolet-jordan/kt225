package kt225.cache

import kt225.common.buffer.RSByteBuffer

/**
 * @author Jordan Abraham
 */
data class CacheFile(
    val nameHash: Int,
    val decompressedLength: Int,
    val compressedLength: Int,
    val offset: Int,
    val data: ByteArray
) {
    fun buffer(): RSByteBuffer = RSByteBuffer(data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CacheFile

        return nameHash == other.nameHash
    }

    override fun hashCode(): Int = nameHash
}
