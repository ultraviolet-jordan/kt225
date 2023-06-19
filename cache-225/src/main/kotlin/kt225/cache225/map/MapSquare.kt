package kt225.cache225.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquare(
    val packed: Int
) {
    constructor(id: Int, x: Int, z: Int) : this((id and 0xffff shl 16) or (x and 0xff shl 8) or (z and 0xff))

    val id: Int get() = packed shr 16 and 0xffff
    val x: Int get() = packed shr 8 and 0xff
    val z: Int get() = packed and 0xff
}
