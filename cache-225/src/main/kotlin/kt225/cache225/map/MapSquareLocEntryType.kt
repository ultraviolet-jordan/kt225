package kt225.cache225.map

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class MapSquareLocEntryType(
    val mapSquare: Int,
    val locs: MutableMap<Int, Array<Long?>> = HashMap()
) : EntryType
