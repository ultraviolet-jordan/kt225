package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquarePosition(
    val packed: Int
) {
    constructor(
        x: Int,
        z: Int,
        plane: Int
    ) : this(x, z, plane, 0)
    
    constructor(
        x: Int,
        z: Int,
        plane: Int,
        layer: Int
    ) : this(
        (x and 0x3f shl 6) 
            or (z and 0x3f) 
            or (plane and 0x3 shl 12)
            or (layer and 0x3 shl 15)
    ) {
        require(x in 0..0x3f)
        require(z in 0..0x3f)
        require(plane in 0..0x3)
        require(layer in 0..0x3)
    }

    inline val x: Int get() = packed shr 6 and 0x3f
    inline val z: Int get() = packed and 0x3f
    inline val plane: Int get() = packed shr 12 and 0x3
    inline val layer: Int get() = packed shr 15 and 0x3
}
