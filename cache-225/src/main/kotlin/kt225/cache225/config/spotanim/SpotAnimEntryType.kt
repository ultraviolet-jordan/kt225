@file:Suppress("DuplicatedCode", "PropertyName")

package kt225.cache225.config.spotanim

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class SpotAnimEntryType(
    val id: Int,
    var model: Int = 0,
    var anim: Int = -1,
    var disposeAlpha: Boolean = false,
    var resizeh: Int = 128,
    var resizev: Int = 128,
    var rotation: Int = 0,
    var ambient: Int = 0,
    var contrast: Int = 0,
    var recol_s: IntArray = IntArray(6),
    var recol_d: IntArray = IntArray(6)
) : EntryType {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpotAnimEntryType

        if (id != other.id) return false
        if (model != other.model) return false
        if (anim != other.anim) return false
        if (disposeAlpha != other.disposeAlpha) return false
        if (resizeh != other.resizeh) return false
        if (resizev != other.resizev) return false
        if (rotation != other.rotation) return false
        if (ambient != other.ambient) return false
        if (contrast != other.contrast) return false
        if (!recol_s.contentEquals(other.recol_s)) return false
        return recol_d.contentEquals(other.recol_d)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + model
        result = 31 * result + anim
        result = 31 * result + disposeAlpha.hashCode()
        result = 31 * result + resizeh
        result = 31 * result + resizev
        result = 31 * result + rotation
        result = 31 * result + ambient
        result = 31 * result + contrast
        result = 31 * result + recol_s.contentHashCode()
        result = 31 * result + recol_d.contentHashCode()
        return result
    }
}
