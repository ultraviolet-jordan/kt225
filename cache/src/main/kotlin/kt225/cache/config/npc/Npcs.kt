package kt225.cache.config.npc

import kt225.cache.EntryType
import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
class Npcs<T : EntryType> : MutableMap<Int, T> by TreeMap()
