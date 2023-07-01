package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLocRotation(
    val id: Int
) {
    init {
        require(id in 0..3)
    }
    
    companion object {
        val SOUTH = MapSquareLocRotation(0)
        val WEST = MapSquareLocRotation(1)
        val NORTH = MapSquareLocRotation(2)
        val EAST = MapSquareLocRotation(3)
    }
}
