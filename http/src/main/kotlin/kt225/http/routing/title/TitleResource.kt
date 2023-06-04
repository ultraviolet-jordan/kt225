package kt225.http.routing.title

/**
 * @author Jordan Abraham
 */
data class TitleResource(
    val crc: Int,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TitleResource

        if (crc != other.crc) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = crc
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
