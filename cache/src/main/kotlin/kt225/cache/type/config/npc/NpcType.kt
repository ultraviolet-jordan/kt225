package kt225.cache.type.config.npc

import kt225.cache.type.Type

/**
 * @author Jordan Abraham
 */
data class NpcType(
    override val id: Int,
    var name: String = "null",
    var description: String = "null",
    var aBoolean76: Boolean = false,
    var size: Int = 1,
    var standSeq: Int = -1,
    var walkSeq: Int = -1,
    var turnAroundSeq: Int = -1,
    var turnRightSeq: Int = -1,
    var turnLeftSeq: Int = -1,
    var disposeAlpha: Boolean = false,
    var anInt97: Int = -1,
    var anInt98: Int = -1,
    var anInt99: Int = -1,
    var showOnMinimap: Boolean = true,
    var level: Int = -1,
    var scaleX: Int = 128,
    var scaleY: Int = 128,
    var options: List<String> = List(5) { "null" },
    var oldColors: List<Int> = emptyList(),
    var newColors: List<Int> = emptyList(),
    var modelIds: List<Int> = emptyList(),
    var headModelIds: List<Int> = emptyList()
) : Type(id)
