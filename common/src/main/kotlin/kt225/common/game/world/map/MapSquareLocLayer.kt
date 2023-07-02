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
        val WALLDECOR = MapSquareLocLayer(1)
        val GROUND = MapSquareLocLayer(2)
        val GROUNDECOR = MapSquareLocLayer(3)
    }
}
