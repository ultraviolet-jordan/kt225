package kt225.cache.config.loc

import kt225.cache.EntryType
import java.util.*

/**
 * @author Jordan Abraham
 */
class Locs<T : EntryType> : MutableMap<Int, T> by TreeMap()
