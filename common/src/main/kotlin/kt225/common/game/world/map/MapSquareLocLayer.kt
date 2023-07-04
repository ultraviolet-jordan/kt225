package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLocLayer(
    val id: Int
) {
    init {
        require(id in 0..3) { "Invalid id: $id" }
    }
    
    companion object {
        val WALL = MapSquareLocLayer(0)
        val WALL_DECOR = MapSquareLocLayer(1)
        val GROUND = MapSquareLocLayer(2)
        val GROUND_DECOR = MapSquareLocLayer(3)
    }
}
