package kt225.cache.config.varp

import kt225.cache.EntryType
import java.util.*

/**
 * @author Jordan Abraham
 */
class Varps<T : EntryType> : MutableMap<Int, T> by TreeMap()
