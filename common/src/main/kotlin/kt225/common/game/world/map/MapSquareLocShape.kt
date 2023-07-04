package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLocShape(
    val id: Int
) {
    /**
     * If this MapSquareLocShape is a wall shape.
     */
    inline val isWall: Boolean 
        get() = layer == MapSquareLocLayer.WALL

    /**
     * If this MapSquareLocShape is a wall decor shape.
     */
    inline val isWallDecor: Boolean 
        get() = layer == MapSquareLocLayer.WALL_DECOR

    /**
     * If this MapSquareLocShape is a roof shape.
     */
    inline val isGround: Boolean 
        get() = layer == MapSquareLocLayer.GROUND

    /**
     * If this MapSquareLocShape is a roof edge shape.
     */
    inline val isGroundDecor: Boolean 
        get() = layer == MapSquareLocLayer.GROUND_DECOR

    /**
     * Returns the MapSquareLocLayer associated with this MapSquareLocShape.
     */
    inline val layer: MapSquareLocLayer 
        get() = when (id) {
            in WALL_STRAIGHT..WALL_SQUARE_CORNER -> MapSquareLocLayer.WALL
            in WALLDECOR_STRAIGHT_NOOFFSET..WALLDECOR_DIAGONAL_BOTH -> MapSquareLocLayer.WALL_DECOR
            in WALL_DIAGONAL..ROOFEDGE_SQUARE_CORNER -> MapSquareLocLayer.GROUND
            GROUND_DECOR.id -> MapSquareLocLayer.GROUND_DECOR
            else -> throw AssertionError("Shape id must be between 0 and 22.")
        }

    companion object {
        val WALL_STRAIGHT = MapSquareLocShape(0)
        val WALL_DIAGONAL_CORNER = MapSquareLocShape(1)
        val WALL_L = MapSquareLocShape(2)
        val WALL_SQUARE_CORNER = MapSquareLocShape(3)
        val WALL_DIAGONAL = MapSquareLocShape(9)
        
        val WALLDECOR_STRAIGHT_NOOFFSET = MapSquareLocShape(4)
        val WALLDECOR_STRAIGHT_OFFSET = MapSquareLocShape(5)
        val WALLDECOR_DIAGONAL_OFFSET = MapSquareLocShape(6)
        val WALLDECOR_DIAGONAL_NOOFFSET = MapSquareLocShape(7)
        val WALLDECOR_DIAGONAL_BOTH = MapSquareLocShape(8)
        
        val ROOF_STRAIGHT = MapSquareLocShape(12)
        val ROOF_DIAGONAL_WITH_ROOFEDGE = MapSquareLocShape(13)
        val ROOF_DIAGONAL = MapSquareLocShape(14)
        val ROOF_L_CONCAVE = MapSquareLocShape(15)
        val ROOF_L_CONVEX = MapSquareLocShape(16)
        val ROOF_FLAT = MapSquareLocShape(17)
        
        val ROOFEDGE_STRAIGHT = MapSquareLocShape(18)
        val ROOFEDGE_DIAGONAL_CORNER = MapSquareLocShape(19)
        val ROOFEDGE_L = MapSquareLocShape(20)
        val ROOFEDGE_SQUARE_CORNER = MapSquareLocShape(21)
        
        val CENTREPIECE_STRAIGHT = MapSquareLocShape(10)
        val CENTREPIECE_DIAGONAL = MapSquareLocShape(11)
        
        val GROUND_DECOR = MapSquareLocShape(22)

        operator fun MapSquareLocShape.rangeTo(other: MapSquareLocShape): IntRange {
            return id..other.id
        }
    }
}
