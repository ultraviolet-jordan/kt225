package kt225.cache225.map

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class MapSquareLandEntryType(
    val mapSquare: Int,
    val lands: LongArray = LongArray(4 * 64 * 64)
) : EntryType
