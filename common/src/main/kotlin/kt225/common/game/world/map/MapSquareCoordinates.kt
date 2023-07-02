package kt225.common.game.world.map

/**
 * ```
 * ===================================================
 * | PROPERTY | DECIMAL | HEX  | BINARY | DIGITS | ===
 * ===================================================
 * | X        | 63      | 0x3f | 111111 | 6      | ===
 * ===================================================
 * | Z        | 63      | 0x3f | 111111 | 6      | ===
 * ===================================================
 * | PLANE    | 3       | 0x3  | 11     | 2      | ===
 * ===================================================
 * ===================================================
 * ===================================================
 * | CAPACITY | 16383 | 0x3fff | 11111111111111 | 14 |
 * ===================================================
 * ```
 *
 * <b>An example of the highest possible bit-packed map coordinates.</b>
 * ```
 * val coord = MapSquareCoordinates(63, 63, 3)
 * assert(16383 == coord.packed)
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
    
    companion object {
        const val COORDINATES_BITS = 6
        const val COORDINATES_MASK = 0x3f
        const val PLANE_BITS = 12
        const val PLANE_MASK = 0x3
    }
}
