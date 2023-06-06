package kt225.common.game.world

/**
 * @author Jordan Abraham
 */
@JvmInline
value class Position(
    private val packedPosition: Int
) {
    constructor(x: Int, z: Int, plane: Int = 0) : this((z and 0x3FFF) or ((x and 0x3FFF) shl 14) or ((plane and 0x3) shl 28))

    val plane get() = packedPosition shr 28 and 0x3
    val x get() = packedPosition shr 14 and 0x3FFF
    val z get() = packedPosition and 0x3FFF

    val mapSquareX get() = (x shr 6)
    val mapSquareZ get() = (z shr 6)
    val mapSquareId get() = (mapSquareX shl 8) or mapSquareZ

    val zoneX get() = (x shr 3)
    val zoneZ get() = (z shr 3)
    val zoneCenterX get() = zoneX - 6
    val zoneCenterZ get() = zoneZ - 6
    val zoneOriginX get() = zoneCenterX shl 3
    val zoneOriginZ get() = zoneCenterZ shl 3
    val zoneId get() = zoneX or (zoneZ shl 11) or (plane shl 22)

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
