package kt225.cache

/**
 * @author Jordan Abraham
 */
class Cache(
    private val archives: Map<Int, CacheArchive?>
) {
    fun openArchives() {
        archives.values.forEach {
            if (it == null) return@forEach
            val bytes = getArchiveResource(it.name()) ?: return@forEach
            it.decode(bytes)
        }
    }

    fun getArchiveResource(name: String): ByteArray? {
        val archive = archives.values.firstOrNull { it?.name() == name } ?: return null
        val resource = javaClass.getResourceAsStream("/archives/${archive.name()}")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return bytes
    }

    fun crcs(): IntArray = IntArray(archives.size) { if (it == 0) 0 else archives[it]?.crc() ?: -1 }
}
