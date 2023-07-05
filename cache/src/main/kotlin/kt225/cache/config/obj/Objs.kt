package kt225.cache.config.obj

import kt225.cache.EntryType
import java.util.*

/**
 * @author Jordan Abraham
 */
class Objs<T : EntryType> : MutableMap<Int, T> by TreeMap()
