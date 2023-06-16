package kt225.cache

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
data class CacheFile(
    val id: Int,
    val nameHash: Int,
    val decompressedLength: Int,
    val compressedLength: Int,
    val offset: Int,
    val data: ByteArray
) {
    fun buffer(): ByteBuffer = ByteBuffer.wrap(data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CacheFile

        return nameHash == other.nameHash
    }

    override fun hashCode(): Int = nameHash
}
