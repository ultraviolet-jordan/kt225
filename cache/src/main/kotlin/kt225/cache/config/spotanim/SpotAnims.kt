package kt225.cache.config.spotanim

import kt225.cache.EntryType
import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
class SpotAnims<T : EntryType> : MutableMap<Int, T> by TreeMap()
