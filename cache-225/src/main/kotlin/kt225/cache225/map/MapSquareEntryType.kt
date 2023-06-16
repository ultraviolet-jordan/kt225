package kt225.cache225.map

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class MapSquareEntryType(
    val entry: MapSquareEntry,
    val lands: Array<MapSquareLandTile?> = arrayOfNulls(4 * 64 * 64),
    val locs: Array<Array<MapSquareLocTile?>?> = arrayOfNulls(4 * 64 * 64),
    var type: Int
) : EntryType
