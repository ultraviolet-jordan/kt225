package kt225.http.routing.inter

/**
 * @author Jordan Abraham
 */
data class InterfaceResource(
    val crc: Int,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InterfaceResource

        if (crc != other.crc) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = crc
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
