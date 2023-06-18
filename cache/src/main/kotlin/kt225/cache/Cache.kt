package kt225.cache

/**
 * @author Jordan Abraham
 */
class Cache(
    private val archives: Map<String, JagArchive?>
) {
    val crcs = archives.map { it.value?.crc() ?: 0 }.toIntArray()

    fun getArchiveResource(name: String): ByteArray? {
        return archives[name]?.zippedBytes()
    }
}
