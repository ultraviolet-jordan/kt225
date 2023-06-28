package kt225.common.game.world

/**
 * @author Jordan Abraham
 */
@JvmInline
value class Position(
    val packed: Int
) {
    constructor(x: Int, z: Int, plane: Int = 0) : this((z and 0x3FFF) or ((x and 0x3FFF) shl 14) or ((plane and 0x3) shl 28))

    inline val plane get() = packed shr 28 and 0x3
    inline val x get() = packed shr 14 and 0x3FFF
    inline val z get() = packed and 0x3FFF

    inline val mapSquareX get() = (x shr 6)
    inline val mapSquareZ get() = (z shr 6)
    inline val mapSquareId get() = mapSquareX shl 8 or mapSquareZ

    inline val zoneX get() = (x shr 3)
    inline val zoneZ get() = (z shr 3)
    inline val zoneCenterX get() = zoneX - 6
    inline val zoneCenterZ get() = zoneZ - 6
    inline val zoneOriginX get() = zoneCenterX shl 3
    inline val zoneOriginZ get() = zoneCenterZ shl 3
    inline val zoneId get() = zoneX or (zoneZ shl 11) or (plane shl 22)

    companion object {
        val None = Position(0, 0, 0)
        val Default = Position(3222, 3222, 0)
    }
}

fun Position.withinDistance(other: Position, distance: Int = 15): Boolean {
    if (other.plane != plane) return false
    val deltaX = other.x - x
    val deltaZ = other.z - z
    return deltaX <= distance && deltaX >= -distance && deltaZ <= distance && deltaZ >= -distance
}
