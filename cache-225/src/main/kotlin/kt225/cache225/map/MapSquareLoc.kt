package kt225.cache225.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLoc(
    val packed: Long
) {
    constructor(
        id: Int,
        x: Int,
        z: Int,
        level: Int,
        shape: Int,
        rotation: Int
    ) : this(((id and 0x1ffff) or ((shape and 0x1f) shl 17) or ((rotation and 0x3) shl 22)).toLong() or (((z and 0x3fff) or ((x and 0x3fff) shl 14) or ((level and 0x3) shl 28)).toLong() shl 24))

    val id: Int get() = (packed and 0x1ffff).toInt()
    val shape: Int get() = (packed shr 17 and 0x1f).toInt()
    val rotation: Int get() = (packed shr 22 and 0x3).toInt()
    val x: Int get() = ((packed shr 24 and 0xffffffffL).toInt() shr 14 and 0x3fff)
    val z: Int get() = ((packed shr 24 and 0xffffffffL).toInt() and 0x3fff)
    val plane: Int get() = ((packed shr 24 and 0xffffffffL).toInt() shr 28 and 0x3f)
}
