package kt225.cache

/**
 * @author Jordan Abraham
 */
data class JagArchiveFile(
    val id: Int,
    val nameHash: Int,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JagArchiveFile

        return id == other.id
    }

    override fun hashCode(): Int = id
}
