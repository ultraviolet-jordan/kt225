package kt225.cache.map

import kt225.cache.EntryType
import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
class MapSquares<T : EntryType> : MutableMap<Int, T> by TreeMap()
