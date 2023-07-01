package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLocShape(
    val id: Int
) {
    inline val isWall: Boolean get() = id in WALL_STRAIGHT..WALL_SQUARE_CORNER || id == WALL_DIAGONAL.id
    inline val isWallDecor: Boolean get() = id in WALLDECOR_STRAIGHT_NOOFFSET..WALLDECOR_DIAGONAL_BOTH
    inline val isRoof: Boolean get() = id in ROOF_STRAIGHT..ROOF_FLAT
    inline val isRoofEdge: Boolean get() = id in ROOFEDGE_STRAIGHT..ROOFEDGE_SQUARE_CORNER
    
    inline val layer: MapSquareLocLayer get() = when (id) {
        in WALL_STRAIGHT..WALL_SQUARE_CORNER -> MapSquareLocLayer.WALL
        in WALLDECOR_STRAIGHT_NOOFFSET..WALLDECOR_DIAGONAL_BOTH -> MapSquareLocLayer.WALLDECOR
        in WALL_DIAGONAL..ROOFEDGE_SQUARE_CORNER -> MapSquareLocLayer.GROUND
        GROUND_DECOR.id -> MapSquareLocLayer.GROUNDECOR
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
