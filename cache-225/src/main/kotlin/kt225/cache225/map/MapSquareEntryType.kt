package kt225.cache225.map

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class MapSquareEntryType(
    val mapSquare: MapSquare,
    val lands: LongArray = LongArray(4 * 64 * 64),
    val locs: Array<LongArray?> = arrayOfNulls(4 * 64 * 64),
    var type: Int
) : EntryType
