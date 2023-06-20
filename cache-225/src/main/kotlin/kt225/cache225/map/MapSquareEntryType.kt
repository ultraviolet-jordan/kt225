package kt225.cache225.map

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class MapSquareEntryType(
    val mapSquare: Int,
    val lands: LongArray = LongArray(4 * 64 * 64),
    val locs: MutableMap<Int, LongArray> = LinkedHashMap(),
    var type: Int
) : EntryType
