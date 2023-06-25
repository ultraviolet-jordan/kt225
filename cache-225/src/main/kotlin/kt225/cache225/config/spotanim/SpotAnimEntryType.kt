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
) : EntryType
