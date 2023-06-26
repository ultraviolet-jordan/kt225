package kt225.cache.config.seq

import kt225.cache.EntryType
import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
class Seqs<T : EntryType> : MutableMap<Int, T> by TreeMap()
