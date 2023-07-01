package kt225.common.game.world.map

/**
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
        (x and COORDINATES_MASK shl COORDINATES_BITS) 
            or (z and COORDINATES_MASK)
            or (id and ID_MASK shl ID_BITS)
    ) {
        require(this.id == id)
        require(this.x == x)
        require(this.z == z)
    }

    /**
     * The id of this MapSquare.
     */
    inline val id: Int 
        get() = packed shr ID_BITS and ID_MASK // 12850

    /**
     * The X-coordinate of this MapSquare within the game world.
     */
    inline val x: Int 
        get() = packed shr COORDINATES_BITS and COORDINATES_MASK // 50

    /**
     * The Z-coordinate of this MapSquare within the game world.
     */
    inline val z: Int 
        get() = packed and COORDINATES_MASK // 50

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
        const val ID_BITS = 16
        const val ID_MASK = 0x7fff
        const val COORDINATES_BITS = 8
        const val COORDINATES_MASK = 0xff
        const val SQUARE_SIZE_BITS = 6
    }
}
