package kt225.cache.type.config.spotanim

import kt225.cache.type.Type

/**
 * @author Jordan Abraham
 */
data class SpotAnimType(
    override val id: Int,
    var modelIndex: Int = 0,
    var sequenceId: Int = -1,
    var disposeAlpha: Boolean = false,
    var oldColors: List<Int> = List(6) { 0 },
    var newColors: List<Int> = List(6) { 0 },
    var breadthScale: Int = 128,
    var depthScale: Int = 128,
    var orientation: Int = 0,
    var ambience: Int = 0,
    var modelShadow: Int = 0
) : Type(id)
