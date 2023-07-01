package kt225.common.game.world

import kt225.common.game.world.map.MapSquare

/**
 * @author Jordan Abraham
 */
@JvmInline
value class Position(
    val packed: Int
) {
    constructor(x: Int, z: Int, plane: Int = 0) : this(
        (z and 0x3fff) 
            or ((x and 0x3fff) shl 14) 
            or ((plane and 0x3) shl 28)
    ) {
        require(x in 0..0x3fff)
        require(z in 0..0x3fff)
        require(plane in 0..0x3)
    }

    inline val plane: Int get() = packed shr 28 and 0x3 // 0
    inline val x: Int get() = packed shr 14 and 0x3fff // 3222
    inline val z: Int get() = packed and 0x3fff // 3222
    
    inline val mapSquare: MapSquare
        get() = MapSquare(
            id = (x shr 6) shl 8 or (z shr 6),
            x = x shr 6,
            z = z shr 6
        )
    
    inline val localX: Int get() = x - mapSquare.mapSquareX // X in map square.
    inline val localZ: Int get() = z - mapSquare.mapSquareZ // Z in map square.

    inline val zoneX: Int get() = (x shr 3) // 402
    inline val zoneZ: Int get() = (z shr 3) // 402
    inline val zoneCenterX: Int get() = zoneX - 6 // 396
    inline val zoneCenterZ: Int get() = zoneZ - 6 // 396
    inline val zoneOriginX: Int get() = zoneCenterX shl 3 // 3168
    inline val zoneOriginZ: Int get() = zoneCenterZ shl 3 // 3168
    inline val zoneId: Int get() = zoneX or (zoneZ shl 11) or (plane shl 22) // 823698

    fun withinDistance(other: Position, distance: Int = 15): Boolean {
        if (other.plane != plane) return false
        val deltaX = other.x - x
        val deltaZ = other.z - z
        return deltaX <= distance && deltaX >= -distance && deltaZ <= distance && deltaZ >= -distance
    }

    companion object {
        val NONE = Position(0)
        val DEFAULT = Position(3222, 3222, 0)
    }
}
