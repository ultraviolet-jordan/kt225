package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLocRotation(
    val id: Int
) {
    init {
        require(id in 0..3) { "Invalid id: $id" }
    }

    companion object {
        val WEST = MapSquareLocRotation(0)
        val NORTH = MapSquareLocRotation(1)
        val EAST = MapSquareLocRotation(2)
        val SOUTH = MapSquareLocRotation(3)
    }
}
