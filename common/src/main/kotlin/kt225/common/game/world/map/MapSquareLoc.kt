package kt225.common.game.world.map

/**
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
            or ((shape.id and SHAPE_MASK) shl SHAPE_BITS)
            or ((rotation.id and ROTATION_MASK) shl ROTATION_BITS)
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
