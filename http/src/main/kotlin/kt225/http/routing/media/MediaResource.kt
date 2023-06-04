package kt225.http.routing.media

/**
 * @author Jordan Abraham
 */
data class MediaResource(
    val crc: Int,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaResource

        if (crc != other.crc) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = crc
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
