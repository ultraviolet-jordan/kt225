package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocShape
import kt225.common.game.world.map.MapSquareLocShape.Companion.WALL_DIAGONAL_CORNER
import kt225.common.game.world.map.MapSquareLocShape.Companion.WALL_L
import kt225.common.game.world.map.MapSquareLocShape.Companion.WALL_SQUARE_CORNER
import kt225.common.game.world.map.MapSquareLocShape.Companion.WALL_STRAIGHT
import kt225.common.game.world.map.collision.wall.changeWallCorner
import kt225.common.game.world.map.collision.wall.changeWallCornerL
import kt225.common.game.world.map.collision.wall.changeWallStraight
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
class WallCollider(
    private val flags: ZoneFlags
) {
    fun change(coordinates: Coordinates, rotation: MapSquareLocRotation, shape: MapSquareLocShape, blockproj: Boolean, add: Boolean) {
        when (shape) {
            WALL_STRAIGHT -> flags.changeWallStraight(coordinates, rotation, blockproj, add)
            WALL_DIAGONAL_CORNER, WALL_SQUARE_CORNER -> flags.changeWallCorner(coordinates, rotation, blockproj, add)
            WALL_L -> flags.changeWallCornerL(coordinates, rotation, blockproj, add)
        }
    }
}
