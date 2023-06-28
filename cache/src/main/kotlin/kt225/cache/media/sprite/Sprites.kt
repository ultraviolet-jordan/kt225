package kt225.cache.media.sprite

import kt225.cache.EntryType
import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
class Sprites<T : EntryType> : MutableMap<Int, T> by TreeMap()
