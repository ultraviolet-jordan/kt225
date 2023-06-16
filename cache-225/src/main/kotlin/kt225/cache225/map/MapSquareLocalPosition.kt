package kt225.cache225.map

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

    val x: Int get() = packed shr 6 and 0x3f
    val z: Int get() = packed and 0x3f
    val plane: Int get() = (packed shr 12)
}
