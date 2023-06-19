package kt225.cache

/**
 * @author Jordan Abraham
 */
data class JagArchiveFile(
    val id: Int,
    val nameHash: Int,
    val crc: Int,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JagArchiveFile

        if (id != other.id) return false
        return nameHash == other.nameHash
    }

    override fun hashCode(): Int {
        return id
    }
}
