package kt225.cache

/**
 * @author Jordan Abraham
 */
class Cache(
    private val archives: Map<String, JagFile?>
) {
    val crcs = archives.map { it.value?.crc ?: 0 }.toIntArray()

    fun getArchiveResource(name: String): ByteArray? {
        return archives[name]?.bytes
    }

    fun release() {
        archives.values.forEach {
            it?.release()
        }
    }
}
