package kt225.cache.type.config.loc

import kt225.cache.type.Type

/**
 * @author Jordan Abraham
 */
data class LocType(
    override val id: Int,
    var modelIds: List<Int> = emptyList(),
    var modelTypes: List<Int> = emptyList(),
    var name: String = "null",
    var description: String = "null",
    var oldColors: List<Int> = emptyList(),
    var newColors: List<Int> = emptyList(),
    var sizeX: Int = 1,
    var sizeZ: Int = 1,
    var hasCollision: Boolean = true,
    var isSolid: Boolean = true,
    var interactable: Boolean = false,
    var adjustToTerrain: Boolean = false,
    var delayShading: Boolean = false,
    var culls: Boolean = false,
    var animationIndex: Int = -1,
    var thickness: Int = 16,
    var brightness: Int = 0,
    var specular: Int = 0,
    var actions: List<String> = List(5) { "null" },
    var disposeAlpha: Boolean = false,
    var mapfunction: Int = -1,
    var mapscene: Int = -1,
    var rotateCounterClockwise: Boolean = false,
    var hasShadow: Boolean = true,
    var scaleX: Int = 128,
    var scaleY: Int = 128,
    var scaleZ: Int = 128,
    var interactionSideFlags: Int = 0,
    var translateX: Int = 0,
    var translateY: Int = 0,
    var translateZ: Int = 0,
    var obstructsGround: Boolean = false
) : Type(id)
