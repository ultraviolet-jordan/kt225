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
        (id and 0xffff)
            or ((shape.id and 0x1f) shl 16)
            or ((rotation.id and 0x3) shl 21)
    ) {
        require(id in 0..0xffff)
        require(shape.id in 0..0x1f)
        require(rotation.id in 0..0x3)
    }

    inline val id: Int get() = (packed and 0xffff)
    inline val shape: MapSquareLocShape get() = MapSquareLocShape((packed shr 16 and 0x1f))
    inline val rotation: MapSquareLocRotation get() = MapSquareLocRotation((packed shr 21 and 0x3))
}
