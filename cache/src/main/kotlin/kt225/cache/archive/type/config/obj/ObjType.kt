package kt225.cache.archive.type.config.obj

import kt225.cache.archive.type.Type

/**
 * @author Jordan Abraham
 */
data class ObjType(
    override val id: Int,
    var modelIndex: Int = 0,
    var name: String = "null",
    var description: String = "null",
    var oldColors: List<Int> = emptyList(),
    var newColors: List<Int> = emptyList(),
    var iconZoom: Int = 2000,
    var iconCameraPitch: Int = 0,
    var iconYaw: Int = 0,
    var iconRoll: Int = 0,
    var iconX: Int = 0,
    var iconY: Int = 0,
    var aBoolean155: Boolean = false,
    var stackable: Boolean = false,
    var value: Int = 1,
    var members: Boolean = false,
    var groundOptions: List<String> = List(5) { "null" },
    var options: List<String> = List(5) { "null" },
    var maleModel0: Int = -1,
    var maleModel1: Int = -1,
    var maleOffsetY: Int = 0,
    var femaleModel0: Int = -1,
    var femaleModel1: Int = -1,
    var femaleOffsetY: Int = 0,
    var maleModel2: Int = -1,
    var femaleModel2: Int = -1,
    var maleHeadModelA: Int = -1,
    var maleHeadModelB: Int = -1,
    var femaleHeadModelA: Int = -1,
    var femaleHeadModelB: Int = -1,
    var stackId: List<Int> = emptyList(),
    var stackAmount: List<Int> = emptyList(),
    var linkedId: Int = -1,
    var certificateId: Int = -1
) : Type(id)
