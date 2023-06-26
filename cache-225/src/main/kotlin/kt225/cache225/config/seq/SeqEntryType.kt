package kt225.cache225.config.seq

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class SeqEntryType(
    val id: Int,
    var framecount: Int = 0,
    var primaryFrames: IntArray? = null,
    var secondaryFrames: IntArray? = null,
    var frameDelay: IntArray? = null,
    var replayOff: Int = -1,
    var labelGroups: IntArray? = null,
    var stretches: Boolean = false,
    var priority: Int = 5,
    var mainhand: Int = -1,
    var offhand: Int = -1,
    var replayCount: Int = 99
) : EntryType
