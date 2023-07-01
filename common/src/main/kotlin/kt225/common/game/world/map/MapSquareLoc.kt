package kt225.common.game.world.map

/**
 * ```
 * ================================================================
 * | PROPERTY | DECIMAL | HEX    | BINARY           | DIGITS | ====
 * ================================================================
 * | ID       | 65535   | 0xffff | 1111111111111111 | 16     | ====
 * ================================================================
 * | SHAPE    | 31      | 0x1f   | 11111            | 5      | ====
 * ================================================================
 * | ROTATION | 3       | 0x3    | 11               | 2      | ====
 * ================================================================
 * ================================================================
 * ================================================================
 * | CAPACITY | 8388607 | 0x7fffff | 11111111111111111111111 | 23 |
 * ================================================================
 * ```
 *
 * <b>An example of the highest possible bit-packed loc.</b>
 * ```
 * val loc = MapSquareLoc(65535, 31, 3)
 * assert(8388607 == loc.packed)
 * ```
 *
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLoc(
    val packed: Int
) {
    constructor(
        id: Int,
        shape: MapSquareLocShape,
        rotation: MapSquareLocRotation
    ) : this(
        (id and ID_MASK)
            or (shape.id and SHAPE_MASK shl SHAPE_BITS)
            or (rotation.id and ROTATION_MASK shl ROTATION_BITS)
    ) {
        require(this.id == id)
        require(this.shape == shape)
        require(this.rotation == rotation)
    }

    /**
     * The id of this loc.
     */
    inline val id: Int 
        get() = (packed and ID_MASK)

    /**
     * The shape of this loc on a MapSquare coordinate.
     */
    inline val shape: MapSquareLocShape 
        get() = MapSquareLocShape((packed shr SHAPE_BITS and SHAPE_MASK))

    /**
     * The rotation of this loc on a MapSquare coordinate.
     */
    inline val rotation: MapSquareLocRotation 
        get() = MapSquareLocRotation((packed shr ROTATION_BITS and ROTATION_MASK))
    
    companion object {
        const val ID_MASK = 0xffff
        const val SHAPE_BITS = 16
        const val SHAPE_MASK = 0x1f
        const val ROTATION_BITS = 21
        const val ROTATION_MASK = 0x3
    }
}
