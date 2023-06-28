package kt225.common.game.world

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLocalPosition(
    val packed: Int
) {
    constructor(
        plane: Int,
        x: Int,
        z: Int
    ) : this((x and 0x3f shl 6) or (z and 0x3f) or (plane shl 12))

    inline val x: Int get() = packed shr 6 and 0x3f
    inline val z: Int get() = packed and 0x3f
    inline val plane: Int get() = packed shr 12
}
