package kt225.cache.type.map

import kt225.cache.type.Type

/**
 * @author Jordan Abraham
 */
data class MapType(
    override val id: Int,
    val regionX: Int,
    val regionZ: Int,
    var collision: Array<Array<ByteArray>> = Array(4) { Array(64) { ByteArray(64) } },
    val locs: Array<Array<Array<MutableList<MapLocType>>>> = Array(4) {
        Array(64) {
            Array(64) {
                mutableListOf()
            }
        }
    }
) : Type(id) {
    data class MapLocType(
        val id: Int,
        val x: Int,
        val z: Int,
        val level: Int,
        val shape: Int,
        val rotation: Int
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MapType

        if (id != other.id) return false
        if (regionX != other.regionX) return false
        if (regionZ != other.regionZ) return false
        if (!collision.contentDeepEquals(other.collision)) return false
        if (!locs.contentDeepEquals(other.locs)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + regionX
        result = 31 * result + regionZ
        result = 31 * result + collision.contentDeepHashCode()
        result = 31 * result + locs.contentDeepHashCode()
        return result
    }
}
