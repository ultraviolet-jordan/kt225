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
        ((height and 0xff) or ((overlayId and 0x7f) shl 8) or ((overlayPath and 0x1f) shl 15)).toLong() 
            or (((overlayRotation and 0x3) or ((collision and 0x1f) shl 2) or ((underlayId and 0x7f) shl 7)).toLong() shl 20)
    ) {
        require(height in 0..0xff)
        require(overlayId in 0..0x7f)
        require(overlayPath in 0..0x1f)
        require(overlayRotation in 0..0x3)
        require(collision in 0..0x1f)
        require(underlayId in 0..0x7f)
    }

    inline val height: Int get() = (packed and 0xff).toInt()
    inline val overlayId: Int get() = (packed shr 8 and 0x7f).toInt()
    inline val overlayPath: Int get() = (packed shr 15 and 0x1f).toInt()
    inline val collision: Int get() = (packed shr 20 and 0xffffffffL).toInt() shr 2 and 0x1f
    inline val overlayRotation: Int get() = (packed shr 20 and 0xffffffff).toInt() and 0x3
    inline val underlayId: Int get() = (packed shr 20 and 0xffffffff).toInt() shr 7 and 0x7f
}
