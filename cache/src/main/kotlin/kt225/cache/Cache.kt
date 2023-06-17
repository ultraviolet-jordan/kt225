package kt225.cache

/**
 * @author Jordan Abraham
 */
class Cache(
    private val archives: Map<Int, JagArchive?>
) {
    val crcs = IntArray(archives.size) { if (it == 0) 0 else archives[it]?.crc() ?: -1 }

    fun getArchiveResource(name: String): ByteArray? {
        return archives.values.firstOrNull { it?.name() == name }?.zipped()
    }
}
