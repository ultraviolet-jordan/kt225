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
        (x and 0xff shl 8) 
            or (z and 0xff)
            or (id and 0x7fff shl 16)
    )

    inline val id: Int get() = packed shr 16 and 0x7fff // 12850
    inline val x: Int get() = packed shr 8 and 0xff // 50
    inline val z: Int get() = packed and 0xff // 50
    inline val mapSquareX: Int get() = x shl 6 // 3200
    inline val mapSquareZ: Int get() = z shl 6 // 3200
}
