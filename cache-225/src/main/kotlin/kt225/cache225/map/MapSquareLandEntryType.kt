package kt225.cache225.map

import kt225.cache.EntryType
import kt225.common.game.world.map.MapSquare

/**
 * @author Jordan Abraham
 */
data class MapSquareLandEntryType(
    val mapSquare: Int,
    val lands: LongArray = LongArray(4 * MapSquare.AREA)
) : EntryType {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MapSquareLandEntryType

        if (mapSquare != other.mapSquare) return false
        return lands.contentEquals(other.lands)
    }

    override fun hashCode(): Int {
        var result = mapSquare
        result = 31 * result + lands.contentHashCode()
        return result
    }
}
