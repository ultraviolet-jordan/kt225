package kt225.cache225.map

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
    ) : this(((height and 0xff) or ((overlayId and 0xff) shl 8) or ((overlayPath and 0xff) shl 16)).toLong() or (((overlayRotation and 0xff) or ((collision and 0xff) shl 8) or ((underlayId and 0xff) shl 16)).toLong() shl 24))

    val height: Int get() = (packed and 0xff).toInt()
    val overlayId: Int get() = (packed shr 8 and 0xff).toInt()
    val overlayPath: Int get() = (packed shr 16 and 0xff).toInt()
    val collision: Int get() = ((packed shr 24 and 0xffffffffL).toInt() shr 8 and 0xff)
    val overlayRotation: Int get() = ((packed shr 24 and 0xffffffffL).toInt() and 0xff)
    val underlayId: Int get() = ((packed shr 24 and 0xffffffffL).toInt() shr 16 and 0xff)
}
