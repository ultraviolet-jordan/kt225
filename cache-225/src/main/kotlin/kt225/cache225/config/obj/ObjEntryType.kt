package kt225.cache225.config.obj

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class ObjEntryType(
    val id: Int,
    var model: Int = 0,
    var name: String? = null,
    var desc: String? = null,
    var zoom2d: Int = 2000,
    var xan2d: Int = 0,
    var yan2d: Int = 0,
    var zan2d: Int = 0,
    var xof2d: Int = 0,
    var yof2d: Int = 0,
    var opcode9: Boolean = false,
    var opcode10: Int = -1,
    var stackable: Boolean = false,
    var cost: Int = 1,
    var members: Boolean = false,
    var manwear: Int = -1,
    var manwear2: Int = -1,
    var manwearOffsetY: Int = 0,
    var womanwear: Int = -1,
    var womanwear2: Int = -1,
    var womanwearOffsetY: Int = 0,
    var manwear3: Int = -1,
    var womanwear3: Int = -1,
    var manhead: Int = -1,
    var manhead2: Int = -1,
    var womanhead: Int = -1,
    var womanhead2: Int = -1,
    var ops: Array<String?>? = null,
    var iops: Array<String?>? = null,
    var recol_s: IntArray? = null,
    var recol_d: IntArray? = null,
    var certlink: Int = -1,
    var certtemplate: Int = -1,
    var countobj: IntArray? = null,
    var countco: IntArray? = null
) : EntryType
