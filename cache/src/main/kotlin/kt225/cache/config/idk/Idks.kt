package kt225.cache.config.idk

import kt225.cache.EntryType
import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
class Idks<T : EntryType> : MutableMap<Int, T> by TreeMap()
