package kt225.common.game.world.map

/**
 * ```
 * =====================================================
 * | PROPERTY | DECIMAL | HEX  | BINARY | DIGITS | =====
 * =====================================================
 * | X        | 63      | 0x3f | 111111 | 6      | =====
 * =====================================================
 * | Z        | 63      | 0x3f | 111111 | 6      | =====
 * =====================================================
 * | PLANE    | 3       | 0x3  | 11     | 2      | =====
 * =====================================================
 * | LAYER    | 3       | 0x3  | 11     | 2      | =====
 * =====================================================
 * =====================================================
 * =====================================================
 * | CAPACITY | 65535 | 0xffff | 1111111111111111 | 16 |
 * =====================================================
 * ```
 *
 * <b>An example of the highest possible bit-packed map coordinates.</b>
 * ```
 * val coord = MapSquareCoordinates(63, 63, 3, 3)
 * assert(65535 == coord.packed)
 * ```
 *
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
        (z and COORDINATES_MASK)
            or (x and COORDINATES_MASK shl COORDINATES_BITS) 
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
        const val LAYER_BITS = 14
        const val LAYER_MASK = 0x3
    }
}
