@file:Suppress("DuplicatedCode", "PropertyName")

package kt225.cache225.config.npc

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class NpcEntryType(
    val id: Int,
    var models: IntArray? = null,
    var name: String? = null,
    var desc: String? = null,
    var size: Int = 1,
    var readyseq: Int = -1,
    var walkseq: Int = -1,
    var disposeAlpha: Boolean = false,
    var walkseq_b: Int = -1,
    var walkseq_r: Int = -1,
    var walkseq_l: Int = -1,
    var ops: Array<String?>? = null,
    var recol_s: IntArray? = null,
    var recol_d: IntArray? = null,
    var headModels: IntArray? = null,
    var opcode90: Int = -1,
    var opcode91: Int = -1,
    var opcode92: Int = -1,
    var visonmap: Boolean = true,
    var vislevel: Int = -1,
    var resizeh: Int = 128,
    var resizev: Int = 128
) : EntryType {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NpcEntryType

        if (id != other.id) return false
        if (models != null) {
            if (other.models == null) return false
            if (!models.contentEquals(other.models)) return false
        } else if (other.models != null) return false
        if (name != other.name) return false
        if (desc != other.desc) return false
        if (size != other.size) return false
        if (readyseq != other.readyseq) return false
        if (walkseq != other.walkseq) return false
        if (disposeAlpha != other.disposeAlpha) return false
        if (walkseq_b != other.walkseq_b) return false
        if (walkseq_r != other.walkseq_r) return false
        if (walkseq_l != other.walkseq_l) return false
        if (ops != null) {
            if (other.ops == null) return false
            if (!ops.contentEquals(other.ops)) return false
        } else if (other.ops != null) return false
        if (recol_s != null) {
            if (other.recol_s == null) return false
            if (!recol_s.contentEquals(other.recol_s)) return false
        } else if (other.recol_s != null) return false
        if (recol_d != null) {
            if (other.recol_d == null) return false
            if (!recol_d.contentEquals(other.recol_d)) return false
        } else if (other.recol_d != null) return false
        if (headModels != null) {
            if (other.headModels == null) return false
            if (!headModels.contentEquals(other.headModels)) return false
        } else if (other.headModels != null) return false
        if (opcode90 != other.opcode90) return false
        if (opcode91 != other.opcode91) return false
        if (opcode92 != other.opcode92) return false
        if (visonmap != other.visonmap) return false
        if (vislevel != other.vislevel) return false
        if (resizeh != other.resizeh) return false
        return resizev == other.resizev
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (models?.contentHashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (desc?.hashCode() ?: 0)
        result = 31 * result + size
        result = 31 * result + readyseq
        result = 31 * result + walkseq
        result = 31 * result + disposeAlpha.hashCode()
        result = 31 * result + walkseq_b
        result = 31 * result + walkseq_r
        result = 31 * result + walkseq_l
        result = 31 * result + (ops?.contentHashCode() ?: 0)
        result = 31 * result + (recol_s?.contentHashCode() ?: 0)
        result = 31 * result + (recol_d?.contentHashCode() ?: 0)
        result = 31 * result + (headModels?.contentHashCode() ?: 0)
        result = 31 * result + opcode90
        result = 31 * result + opcode91
        result = 31 * result + opcode92
        result = 31 * result + visonmap.hashCode()
        result = 31 * result + vislevel
        result = 31 * result + resizeh
        result = 31 * result + resizev
        return result
    }
}
