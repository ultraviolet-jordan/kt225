@file:Suppress("DuplicatedCode", "PropertyName")

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
) : EntryType {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ObjEntryType

        if (id != other.id) return false
        if (model != other.model) return false
        if (name != other.name) return false
        if (desc != other.desc) return false
        if (zoom2d != other.zoom2d) return false
        if (xan2d != other.xan2d) return false
        if (yan2d != other.yan2d) return false
        if (zan2d != other.zan2d) return false
        if (xof2d != other.xof2d) return false
        if (yof2d != other.yof2d) return false
        if (opcode9 != other.opcode9) return false
        if (opcode10 != other.opcode10) return false
        if (stackable != other.stackable) return false
        if (cost != other.cost) return false
        if (members != other.members) return false
        if (manwear != other.manwear) return false
        if (manwear2 != other.manwear2) return false
        if (manwearOffsetY != other.manwearOffsetY) return false
        if (womanwear != other.womanwear) return false
        if (womanwear2 != other.womanwear2) return false
        if (womanwearOffsetY != other.womanwearOffsetY) return false
        if (manwear3 != other.manwear3) return false
        if (womanwear3 != other.womanwear3) return false
        if (manhead != other.manhead) return false
        if (manhead2 != other.manhead2) return false
        if (womanhead != other.womanhead) return false
        if (womanhead2 != other.womanhead2) return false
        if (ops != null) {
            if (other.ops == null) return false
            if (!ops.contentEquals(other.ops)) return false
        } else if (other.ops != null) return false
        if (iops != null) {
            if (other.iops == null) return false
            if (!iops.contentEquals(other.iops)) return false
        } else if (other.iops != null) return false
        if (recol_s != null) {
            if (other.recol_s == null) return false
            if (!recol_s.contentEquals(other.recol_s)) return false
        } else if (other.recol_s != null) return false
        if (recol_d != null) {
            if (other.recol_d == null) return false
            if (!recol_d.contentEquals(other.recol_d)) return false
        } else if (other.recol_d != null) return false
        if (certlink != other.certlink) return false
        if (certtemplate != other.certtemplate) return false
        if (countobj != null) {
            if (other.countobj == null) return false
            if (!countobj.contentEquals(other.countobj)) return false
        } else if (other.countobj != null) return false
        if (countco != null) {
            if (other.countco == null) return false
            if (!countco.contentEquals(other.countco)) return false
        } else if (other.countco != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + model
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (desc?.hashCode() ?: 0)
        result = 31 * result + zoom2d
        result = 31 * result + xan2d
        result = 31 * result + yan2d
        result = 31 * result + zan2d
        result = 31 * result + xof2d
        result = 31 * result + yof2d
        result = 31 * result + opcode9.hashCode()
        result = 31 * result + opcode10
        result = 31 * result + stackable.hashCode()
        result = 31 * result + cost
        result = 31 * result + members.hashCode()
        result = 31 * result + manwear
        result = 31 * result + manwear2
        result = 31 * result + manwearOffsetY
        result = 31 * result + womanwear
        result = 31 * result + womanwear2
        result = 31 * result + womanwearOffsetY
        result = 31 * result + manwear3
        result = 31 * result + womanwear3
        result = 31 * result + manhead
        result = 31 * result + manhead2
        result = 31 * result + womanhead
        result = 31 * result + womanhead2
        result = 31 * result + (ops?.contentHashCode() ?: 0)
        result = 31 * result + (iops?.contentHashCode() ?: 0)
        result = 31 * result + (recol_s?.contentHashCode() ?: 0)
        result = 31 * result + (recol_d?.contentHashCode() ?: 0)
        result = 31 * result + certlink
        result = 31 * result + certtemplate
        result = 31 * result + (countobj?.contentHashCode() ?: 0)
        result = 31 * result + (countco?.contentHashCode() ?: 0)
        return result
    }
}
