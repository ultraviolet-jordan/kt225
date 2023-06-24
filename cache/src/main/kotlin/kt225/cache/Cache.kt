package kt225.cache

/**
 * @author Jordan Abraham
 */
class Cache(
    private val jagFiles: Map<String, JagFile?>
) {
    val crcs = jagFiles.map { it.value?.crc ?: 0 }.toIntArray()

    fun getJagFileBytes(name: String): ByteArray? {
        return jagFiles[name]?.bytes
    }

    fun release() {
        jagFiles.values.forEach {
            it?.release()
        }
    }
}
