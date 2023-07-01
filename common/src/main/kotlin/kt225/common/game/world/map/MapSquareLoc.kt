package kt225.common.game.world.map

/**
 * ```
 * =======================================================================================
 * | PROPERTY | DECIMAL | HEX    | BINARY           | DIGITS | ===========================
 * =======================================================================================
 * | ID       | 65535   | 0xffff | 1111111111111111 | 16     | ===========================
 * =======================================================================================
 * | SHAPE    | 31      | 0x1f   | 11111            | 5      | ===========================
 * =======================================================================================
 * | ROTATION | 3       | 0x3    | 11               | 2      | ===========================
 * =======================================================================================
 * | X        | 63      | 0x3f   | 111111           | 6      | ===========================
 * =======================================================================================
 * | Z        | 63      | 0x3f   | 111111           | 6      | ===========================
 * =======================================================================================
 * | PLANE    | 3       | 0x3    | 11               | 2      | ===========================
 * =======================================================================================
 * =======================================================================================
 * =======================================================================================
 * | CAPACITY | 137438953471 | 0x1fffffffff | 1111111111111111111111111111111111111 | 37 |
 * =======================================================================================
 * ```
 *
 * <b>An example of the highest possible bit-packed loc.</b>
 * ```
 * val loc = MapSquareLoc(65535, 31, 3, 63, 63, 3)
 * assert(137438953471 == loc.packed)
 * ```
 *
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLoc(
    val packed: Long
) {
    constructor(
        id: Int,
        shape: MapSquareLocShape,
        rotation: MapSquareLocRotation,
        coords: MapSquareCoordinates,
    ) : this(
        (id.toLong() and ID_MASK)
            or (shape.id.toLong() and SHAPE_MASK shl SHAPE_BITS)
            or (rotation.id.toLong() and ROTATION_MASK shl ROTATION_BITS)
            or (coords.packed.toLong() and COORDINATES_MASK shl COORDINATES_BITS)
    ) {
        require(this.id == id)
        require(this.shape == shape)
        require(this.rotation == rotation)
        require(this.coords == coords)
    }

    /**
     * The id of this loc.
     */
    inline val id: Int 
        get() = ((packed and ID_MASK).toInt())

    /**
     * The shape of this loc on a MapSquare coordinate.
     */
    inline val shape: MapSquareLocShape 
        get() = MapSquareLocShape(((packed shr SHAPE_BITS and SHAPE_MASK).toInt()))

    /**
     * The rotation of this loc on a MapSquare coordinate.
     */
    inline val rotation: MapSquareLocRotation 
        get() = MapSquareLocRotation(((packed shr ROTATION_BITS and ROTATION_MASK).toInt()))

    /**
     * The MapSquareCoordinates of this loc.
     */
    inline val coords: MapSquareCoordinates
        get() = MapSquareCoordinates((packed shr COORDINATES_BITS and COORDINATES_MASK).toInt())
    
    companion object {
        const val ID_MASK = 0xffffL
        const val SHAPE_BITS = 16
        const val SHAPE_MASK = 0x1fL
        const val ROTATION_BITS = 21
        const val ROTATION_MASK = 0x3L
        const val COORDINATES_BITS = 23
        const val COORDINATES_MASK = 0x3fffL
    }
}
