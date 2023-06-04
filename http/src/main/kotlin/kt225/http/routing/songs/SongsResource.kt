package kt225.http.routing.songs

/**
 * @author Jordan Abraham
 */
data class SongsResource(
    val name: String,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongsResource

        if (name != other.name) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
