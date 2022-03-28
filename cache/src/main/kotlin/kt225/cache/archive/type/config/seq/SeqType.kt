package kt225.cache.archive.type.config.seq

import kt225.cache.archive.type.Type

/**
 * @author Jordan Abraham
 */
data class SeqType(
    override val id: Int,
    var delay: Int = -1,
    var renderPadding: Boolean = false,
    var priority: Int = 5,
    var shieldOverride: Int = -1,
    var weaponOverride: Int = -1,
    var replays: Int = 99,
    var frameCount: Int = 0,
    var primaryFrames: List<Int> = emptyList(),
    var secondaryFrames: List<Int> = emptyList(),
    var frameDelay: List<Int> = emptyList(),
    var labelGroups: List<Int> = emptyList()
) : Type(id)
