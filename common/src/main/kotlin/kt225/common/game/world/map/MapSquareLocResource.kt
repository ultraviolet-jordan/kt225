package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLocResource(
    val packed: Long
) {
    constructor(
        position: Int,
        loc: Int
    ) : this((position and 0x3ffff).toLong() or ((loc and 0xffffff).toLong() shl 18)) {
        require(position in 0..0x3ffff)
        require(loc in 0..0xffffff)
    }

    inline val position: Int get() = (packed and 0x3ffff).toInt()
    inline val loc: Int get() = (packed shr 18 and 0xffffff).toInt()
}
