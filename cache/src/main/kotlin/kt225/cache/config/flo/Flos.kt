package kt225.cache.config.flo

import kt225.cache.EntryType
import java.util.*

/**
 * @author Jordan Abraham
 */
class Flos<T : EntryType> : MutableMap<Int, T> by TreeMap()
