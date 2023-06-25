package kt225.cache225.config.idk

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class IdkEntryType(
    val id: Int,
    var type: Int = -1,
    var models: IntArray? = null,
    var disable: Boolean = false,
    var recol_s: IntArray = IntArray(6),
    var recol_d: IntArray = IntArray(6),
    var headModels: IntArray = IntArray(5) { -1 }
) : EntryType
