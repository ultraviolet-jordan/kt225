package kt225.common.game.world.map

/**
 * ```
 * ============================================================================
 * | PROPERTY | DECIMAL | HEX    | BINARY         | DIGITS | ==================
 * ============================================================================
 * | ID       | 16383   | 0x3fff | 11111111111111 | 14     | ==================
 * ============================================================================
 * | X        | 255     | 0xff   | 11111111       | 8      | ==================
 * ============================================================================
 * | Z        | 255     | 0xff   | 11111111       | 8      | ==================
 * ============================================================================
 * ============================================================================
 * ============================================================================
 * | CAPACITY | 1073741823 | 0x3fffffff | 111111111111111111111111111111 | 30 |
 * ============================================================================
 * ```
 *
 * <b>An example of the highest possible bit-packed map square.</b>
 * ```
 * val mapSquare = MapSquare(16383, 255, 255)
 * assert(1073741823 == mapSquare.packed)
 * ```
 *
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquare(
    val packed: Int
) {
    constructor(
        id: Int,
        x: Int,
        z: Int
    ) : this(
        (id and ID_MASK)
            or (x and COORDINATES_MASK shl X_BITS) 
            or (z and COORDINATES_MASK shl Z_BITS)
    ) {
        require(this.id == id)
        require(this.x == x)
        require(this.z == z)
    }

    /**
     * The id of this MapSquare.
     */
    inline val id: Int 
        get() = packed and ID_MASK // 12850

    /**
     * The X-coordinate of this MapSquare within the game world.
     */
    inline val x: Int 
        get() = packed shr X_BITS and COORDINATES_MASK // 50

    /**
     * The Z-coordinate of this MapSquare within the game world.
     */
    inline val z: Int 
        get() = packed shr Z_BITS and COORDINATES_MASK // 50

    /**
     * The starting X-coordinate of this MapSquare.
     * The bottom left corner.
     */
    inline val mapSquareX: Int 
        get() = x shl SQUARE_SIZE_BITS // 3200

    /**
     * The starting Z-coordinate of this MapSquare.
     * The bottom left corner.
     */
    inline val mapSquareZ: Int 
        get() = z shl SQUARE_SIZE_BITS // 3200
    
    companion object {
        const val AREA = 64 * 64
        const val ID_MASK = 4 * AREA - 1
        const val X_BITS = 14
        const val Z_BITS = 22
        const val COORDINATES_MASK = 0xff
        const val SQUARE_SIZE_BITS = 6
    }
}
