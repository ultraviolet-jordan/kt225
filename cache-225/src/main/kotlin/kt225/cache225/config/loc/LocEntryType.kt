@file:Suppress("DuplicatedCode", "PropertyName")

package kt225.cache225.config.loc

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class LocEntryType(
    val id: Int,
    var models: IntArray? = null,
    var shapes: IntArray? = null,
    var name: String? = null,
    var desc: String? = null,
    var width: Int = 1,
    var length: Int = 1,
    var blockwalk: Boolean = true,
    var blockrange: Boolean = true,
    var interactive: Int = -1,
    var interactable: Boolean = false,
    var hillskew: Boolean = false,
    var computeVertexColors: Boolean = false,
    var occlude: Boolean = false,
    var seq: Int = -1,
    var disposeAlpha: Boolean = false,
    var walloff: Int = 16,
    var ambient: Int = 0,
    var ops: Array<String?>? = null,
    var contrast: Int = 0,
    var recol_s: IntArray? = null,
    var recol_d: IntArray? = null,
    var mapfunction: Int = -1,
    var mirror: Boolean = false,
    var active: Boolean = true,
    var resizex: Int = 128,
    var resizey: Int = 128,
    var resizez: Int = 128,
    var mapscene: Int = -1,
    var blocksides: Int = 0,
    var xoff: Int = 0,
    var yoff: Int = 0,
    var zoff: Int = 0,
    var forcedecor: Boolean = false
) : EntryType {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocEntryType

        if (id != other.id) return false
        if (models != null) {
            if (other.models == null) return false
            if (!models.contentEquals(other.models)) return false
        } else if (other.models != null) return false
        if (shapes != null) {
            if (other.shapes == null) return false
            if (!shapes.contentEquals(other.shapes)) return false
        } else if (other.shapes != null) return false
        if (name != other.name) return false
        if (desc != other.desc) return false
        if (width != other.width) return false
        if (length != other.length) return false
        if (blockwalk != other.blockwalk) return false
        if (blockrange != other.blockrange) return false
        if (interactive != other.interactive) return false
        if (interactable != other.interactable) return false
        if (hillskew != other.hillskew) return false
        if (computeVertexColors != other.computeVertexColors) return false
        if (occlude != other.occlude) return false
        if (seq != other.seq) return false
        if (disposeAlpha != other.disposeAlpha) return false
        if (walloff != other.walloff) return false
        if (ambient != other.ambient) return false
        if (ops != null) {
            if (other.ops == null) return false
            if (!ops.contentEquals(other.ops)) return false
        } else if (other.ops != null) return false
        if (contrast != other.contrast) return false
        if (recol_s != null) {
            if (other.recol_s == null) return false
            if (!recol_s.contentEquals(other.recol_s)) return false
        } else if (other.recol_s != null) return false
        if (recol_d != null) {
            if (other.recol_d == null) return false
            if (!recol_d.contentEquals(other.recol_d)) return false
        } else if (other.recol_d != null) return false
        if (mapfunction != other.mapfunction) return false
        if (mirror != other.mirror) return false
        if (active != other.active) return false
        if (resizex != other.resizex) return false
        if (resizey != other.resizey) return false
        if (resizez != other.resizez) return false
        if (mapscene != other.mapscene) return false
        if (blocksides != other.blocksides) return false
        if (xoff != other.xoff) return false
        if (yoff != other.yoff) return false
        if (zoff != other.zoff) return false
        return forcedecor == other.forcedecor
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (models?.contentHashCode() ?: 0)
        result = 31 * result + (shapes?.contentHashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (desc?.hashCode() ?: 0)
        result = 31 * result + width
        result = 31 * result + length
        result = 31 * result + blockwalk.hashCode()
        result = 31 * result + blockrange.hashCode()
        result = 31 * result + interactive
        result = 31 * result + interactable.hashCode()
        result = 31 * result + hillskew.hashCode()
        result = 31 * result + computeVertexColors.hashCode()
        result = 31 * result + occlude.hashCode()
        result = 31 * result + seq
        result = 31 * result + disposeAlpha.hashCode()
        result = 31 * result + walloff
        result = 31 * result + ambient
        result = 31 * result + (ops?.contentHashCode() ?: 0)
        result = 31 * result + contrast
        result = 31 * result + (recol_s?.contentHashCode() ?: 0)
        result = 31 * result + (recol_d?.contentHashCode() ?: 0)
        result = 31 * result + mapfunction
        result = 31 * result + mirror.hashCode()
        result = 31 * result + active.hashCode()
        result = 31 * result + resizex
        result = 31 * result + resizey
        result = 31 * result + resizez
        result = 31 * result + mapscene
        result = 31 * result + blocksides
        result = 31 * result + xoff
        result = 31 * result + yoff
        result = 31 * result + zoff
        result = 31 * result + forcedecor.hashCode()
        return result
    }
}
