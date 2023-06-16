package kt225.cache225.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLandTileOverlay(
    val packed: Int
) {
    constructor(id: Int, path: Int, rotation: Int) : this((id and 0xff shr 16) or (path and 0xff shr 8) or (rotation and 0xff))

    val id: Int get() = packed shr 16 and 0xff
    val path: Int get() = packed shr 8 and 0xff
    val rotation: Int get() = packed and 0xff
}
