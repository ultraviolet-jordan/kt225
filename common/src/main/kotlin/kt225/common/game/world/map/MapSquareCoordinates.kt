package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareCoordinates(
    val packed: Int
) {
    constructor(
        x: Int,
        z: Int,
        plane: Int
    ) : this(x, z, plane, 0)
    
    constructor(
        x: Int,
        z: Int,
        plane: Int,
        layer: Int
    ) : this(
        (x and COORDINATES_MASK shl COORDINATES_BITS) 
            or (z and COORDINATES_MASK) 
            or (plane and PLANE_MASK shl PLANE_BITS)
            or (layer and LAYER_MASK shl LAYER_BITS)
    ) {
        require(this.x == x)
        require(this.z == z)
        require(this.plane == plane)
        require(this.layer == layer)
    }

    /**
     * The X-coordinate of a tile within this MapSquare.
     */
    inline val x: Int 
        get() = packed shr COORDINATES_BITS and COORDINATES_MASK // 63

    /**
     * The Z-coordinate of a tile within this MapSquare.
     */
    inline val z: Int 
        get() = packed and COORDINATES_MASK // 63

    /**
     * The plane of a tile within this MapSquare.
     */
    inline val plane: Int 
        get() = packed shr PLANE_BITS and PLANE_MASK

    /**
     * The layer of a tile within this MapSquare.
     */
    inline val layer: Int 
        get() = packed shr LAYER_BITS and LAYER_MASK
    
    companion object {
        const val COORDINATES_BITS = 6
        const val COORDINATES_MASK = 0x3f
        const val PLANE_BITS = 12
        const val PLANE_MASK = 0x3
        const val LAYER_BITS = 15
        const val LAYER_MASK = 0x3
    }
}
