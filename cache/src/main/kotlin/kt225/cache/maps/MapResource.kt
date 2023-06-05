package kt225.cache.maps

/**
 * @author Jordan Abraham
 */
data class MapResource(
    val name: String,
    val bytes: ByteArray,
    val crc: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MapResource

        if (name != other.name) return false
        if (!bytes.contentEquals(other.bytes)) return false
        return crc == other.crc
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + bytes.contentHashCode()
        result = 31 * result + crc
        return result
    }
}
