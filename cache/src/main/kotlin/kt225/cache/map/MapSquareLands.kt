package kt225.cache.map

import kt225.cache.EntryType
import java.util.concurrent.ConcurrentSkipListMap

/**
 * @author Jordan Abraham
 */
class MapSquareLands<T : EntryType> : MutableMap<Int, T> by ConcurrentSkipListMap()
