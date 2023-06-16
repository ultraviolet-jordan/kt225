package kt225.cache.archive.config.varp

import kt225.cache.EntryType
import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
class Varps<T : EntryType> : MutableMap<Int, T> by TreeMap()
