package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLand(
    val packed: Long
) {
    constructor(
        height: Int,
        overlayId: Int,
        overlayPath: Int,
        overlayRotation: Int,
        collision: Int,
        underlayId: Int
    ) : this(
        (height.toLong() and HEIGHT_MASK)
            or (overlayId.toLong() and OVERLAY_MASK shl OVERLAY_BITS)
            or (overlayPath.toLong() and OVERLAY_PATH_MASK shl OVERLAY_PATH_BITS)
            or (overlayRotation.toLong() and OVERLAY_ROTATION_MASK shl OVERLAY_ROTATION_BITS)
            or (collision.toLong() and COLLISION_MASK shl COLLISION_BITS)
            or (underlayId.toLong() and UNDERLAY_MASK shl UNDERLAY_BITS)
    ) {
        require(this.height == height)
        require(this.overlayId == overlayId)
        require(this.overlayPath == overlayPath)
        require(this.overlayRotation == overlayRotation)
        require(this.collision == collision)
        require(this.underlayId == underlayId)
    }

    /**
     * This height of this land on a MapSquare coordinate.
     */
    inline val height: Int 
        get() = (packed and HEIGHT_MASK).toInt()

    /**
     * The overlay of this land on a MapSquare coordinate.
     */
    inline val overlayId: Int 
        get() = (packed shr OVERLAY_BITS and OVERLAY_MASK).toInt()

    /**
     * The overlay path of this land on a MapSquare coordinate.
     */
    inline val overlayPath: Int 
        get() = (packed shr OVERLAY_PATH_BITS and OVERLAY_PATH_MASK).toInt()

    /**
     * The overlay rotation of this land on a MapSquare coordinate.
     */
    inline val overlayRotation: Int
        get() = (packed shr OVERLAY_ROTATION_BITS and OVERLAY_ROTATION_MASK).toInt()

    /**
     * The collision flags of this land on a MapSquare coordinate.
     */
    inline val collision: Int 
        get() = (packed shr COLLISION_BITS and COLLISION_MASK).toInt()

    /**
     * The underlay of this land on a MapSquare coordinate.
     */
    inline val underlayId: Int 
        get() = (packed shr UNDERLAY_BITS and UNDERLAY_MASK).toInt()
    
    companion object {
        const val HEIGHT_MASK = 0xffL
        const val OVERLAY_BITS = 8
        const val OVERLAY_MASK = 0x7fL
        const val OVERLAY_PATH_BITS = 15
        const val OVERLAY_PATH_MASK = 0x1fL
        const val OVERLAY_ROTATION_BITS = 20
        const val OVERLAY_ROTATION_MASK = 0x3L
        const val COLLISION_BITS = 23
        const val COLLISION_MASK = 0x1fL
        const val UNDERLAY_BITS = 28
        const val UNDERLAY_MASK = 0x7fL
    }
}
