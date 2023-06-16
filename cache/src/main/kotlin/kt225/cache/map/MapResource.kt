package kt225.cache.map

/**
 * @author Jordan Abraham
 */
data class MapResource(
    val name: String,
    val id: Int,
    val x: Int,
    val z: Int,
    val type: Int,
    val bytes: ByteArray,
    val crc: Int
)
