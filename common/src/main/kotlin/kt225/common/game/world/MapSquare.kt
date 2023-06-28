package kt225.common.game.world

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquare(
    val packed: Int
) {
    constructor(id: Int, x: Int, z: Int) : this((id and 0xffff shl 16) or (x and 0xff shl 8) or (z and 0xff))

    inline val id: Int get() = packed shr 16 and 0xffff
    inline val x: Int get() = packed shr 8 and 0xff
    inline val z: Int get() = packed and 0xff
}
