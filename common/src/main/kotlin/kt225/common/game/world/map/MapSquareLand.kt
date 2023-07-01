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
        ((height and HEIGHT_MASK) or ((overlayId and OVERLAY_MASK) shl OVERLAY_BITS) or ((overlayPath and OVERLAY_PATH_MASK) shl OVERLAY_PATH_BITS)).toLong() 
            or (((overlayRotation and OVERLAY_ROTATION_MASK) or ((collision and COLLISION_MASK) shl COLLISION_BITS) or ((underlayId and UNDERLAY_MASK) shl UNDERLAY_BITS)).toLong() shl LONG_BITS)
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
        get() = (packed and HEIGHT_MASK_LONG).toInt()

    /**
     * The overlay of this land on a MapSquare coordinate.
     */
    inline val overlayId: Int 
        get() = (packed shr OVERLAY_BITS and OVERLAY_MASK_LONG).toInt()

    /**
     * The overlay path of this land on a MapSquare coordinate.
     */
    inline val overlayPath: Int 
        get() = (packed shr OVERLAY_PATH_BITS and OVERLAY_PATH_MASK_LONG).toInt()

    /**
     * The collision flags of this land on a MapSquare coordinate.
     */
    inline val collision: Int 
        get() = (packed shr LONG_BITS and INT_MASK).toInt() shr COLLISION_BITS and COLLISION_MASK

    /**
     * The overlay rotation of this land on a MapSquare coordinate.
     */
    inline val overlayRotation: Int 
        get() = (packed shr LONG_BITS and INT_MASK).toInt() and OVERLAY_ROTATION_MASK

    /**
     * The underlay of this land on a MapSquare coordinate.
     */
    inline val underlayId: Int 
        get() = (packed shr LONG_BITS and INT_MASK).toInt() shr UNDERLAY_BITS and UNDERLAY_MASK
    
    companion object {
        const val HEIGHT_MASK = 0xff
        const val HEIGHT_MASK_LONG = HEIGHT_MASK.toLong()
        const val OVERLAY_BITS = 8
        const val OVERLAY_MASK = 0x7f
        const val OVERLAY_MASK_LONG = OVERLAY_MASK.toLong()
        const val OVERLAY_PATH_BITS = 15
        const val OVERLAY_PATH_MASK = 0x1f
        const val OVERLAY_PATH_MASK_LONG = 0x1f.toLong()
        const val LONG_BITS = OVERLAY_PATH_BITS + OVERLAY_PATH_MASK
        const val INT_MASK = 0xffffffff
        const val OVERLAY_ROTATION_MASK = 0x3
        const val COLLISION_BITS = 2
        const val COLLISION_MASK = 0x1f
        const val UNDERLAY_BITS = 7
        const val UNDERLAY_MASK = 0x7f
    }
}
