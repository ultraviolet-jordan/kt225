package kt225.common.game.world

import kt225.common.game.world.map.MapSquare

/**
 * @author Jordan Abraham
 */
@JvmInline
value class Coordinates(
    val packed: Int
) {
    constructor(
        x: Int,
        z: Int,
        plane: Int
    ) : this(
        (z and COORDINATES_MASK) 
            or (x and COORDINATES_MASK shl COORDINATES_BITS) 
            or (plane and PLANE_MASK shl PLANE_BITS)
    ) {
        require(this.x == x) { "Invalid x: $x" }
        require(this.z == z) { "Invalid z: $z" }
        require(this.plane == plane) { "Invalid plane: $plane" }
    }

    /**
     * The X-coordinate of a tile in the game world.
     */
    inline val x: Int 
        get() = packed shr COORDINATES_BITS and COORDINATES_MASK // 3222

    /**
     * The Z-coordinate of a tile in the game world.
     */
    inline val z: Int 
        get() = packed and COORDINATES_MASK // 3222

    /**
     * The plane of a tile in the game world.
     */
    inline val plane: Int
        get() = packed shr PLANE_BITS and PLANE_MASK // 0

    /**
     * The MapSquare these Coordinates are inside.
     */
    inline val mapSquare: MapSquare
        get() = MapSquare(
            id = (x shr SQUARE_SIZE_BITS) shl 8 or (z shr SQUARE_SIZE_BITS),
            x = x shr SQUARE_SIZE_BITS,
            z = z shr SQUARE_SIZE_BITS
        )

    /**
     * The local X-coordinate of a tile inside a MapSquare.
     */
    inline val localX: Int 
        get() = x - mapSquare.mapSquareX // X in map square.

    /**
     * The local Z-coordinate of a tile inside a MapSquare.
     */
    inline val localZ: Int 
        get() = z - mapSquare.mapSquareZ // Z in map square.

    /**
     * The X-coordinate of the Zone this coordinate is inside.
     */
    inline val zoneX: Int 
        get() = x shr ZONE_SHIFT // 402

    /**
     * The Z-coordinate of the Zone this coordinate is inside.
     */
    inline val zoneZ: Int 
        get() = z shr ZONE_SHIFT // 402
    
    inline val zoneCenterX: Int 
        get() = zoneX - ZONE_CENTER_OFFSET // 396
    
    inline val zoneCenterZ: Int 
        get() = zoneZ - ZONE_CENTER_OFFSET // 396
    
    inline val zoneOriginX: Int 
        get() = zoneCenterX shl ZONE_SHIFT // 3168
    
    inline val zoneOriginZ: Int 
        get() = zoneCenterZ shl ZONE_SHIFT // 3168
    
    inline val zoneId: Int
        get() = zoneX or (zoneZ shl ZONE_ID_SHIFT) or (plane shl ZONE_ID_PLANE_SHIFT) // 823698

    fun withinDistance(other: Coordinates, distance: Int = 15): Boolean {
        if (other.plane != plane) return false
        val deltaX = other.x - x
        val deltaZ = other.z - z
        return deltaX <= distance && deltaX >= -distance && deltaZ <= distance && deltaZ >= -distance
    }

    fun transform(deltaX: Int, deltaZ: Int, deltaPlane: Int = 0): Coordinates {
        return Coordinates(x + deltaX, z + deltaZ, plane + deltaPlane)
    }

    companion object {
        val NONE = Coordinates(0)
        val DEFAULT = Coordinates(3073, 3277, 0)
        
        const val PLANE_BITS = 28
        const val PLANE_MASK = 0x3
        const val COORDINATES_BITS = 14
        const val COORDINATES_MASK = 0x3fff
        const val SQUARE_SIZE_BITS = 6
        const val ZONE_SHIFT = 3
        const val ZONE_CENTER_OFFSET = 6
        const val ZONE_ID_SHIFT = 11
        const val ZONE_ID_PLANE_SHIFT = 22
    }
}
