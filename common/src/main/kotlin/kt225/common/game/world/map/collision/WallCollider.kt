package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocRotation.Companion.EAST
import kt225.common.game.world.map.MapSquareLocRotation.Companion.NORTH
import kt225.common.game.world.map.MapSquareLocRotation.Companion.SOUTH
import kt225.common.game.world.map.MapSquareLocRotation.Companion.WEST
import kt225.common.game.world.map.MapSquareLocShape
import kt225.common.game.world.map.MapSquareLocShape.Companion.WALL_DIAGONAL_CORNER
import kt225.common.game.world.map.MapSquareLocShape.Companion.WALL_L
import kt225.common.game.world.map.MapSquareLocShape.Companion.WALL_SQUARE_CORNER
import kt225.common.game.world.map.MapSquareLocShape.Companion.WALL_STRAIGHT
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_WEST

/**
 * @author Jordan Abraham
 */
class WallCollider(
    private val collider: Collider
) {
    fun change(coordinates: Coordinates, rotation: MapSquareLocRotation, shape: MapSquareLocShape, add: Boolean) {
        when (shape) {
            WALL_STRAIGHT -> when (rotation) {
                WEST -> {
                    collider.changeCollision(coordinates, WALL_WEST, add)
                    collider.changeCollision(coordinates.transform(-1, 0), WALL_EAST, add)
                }
                NORTH -> {
                    collider.changeCollision(coordinates, WALL_NORTH, add)
                    collider.changeCollision(coordinates.transform(0, 1), WALL_SOUTH, add)
                }
                EAST -> {
                    collider.changeCollision(coordinates, WALL_EAST, add)
                    collider.changeCollision(coordinates.transform(1, 0), WALL_WEST, add)
                }
                SOUTH -> {
                    collider.changeCollision(coordinates, WALL_SOUTH, add)
                    collider.changeCollision(coordinates.transform(0, -1), WALL_NORTH, add)
                }
            }
            WALL_DIAGONAL_CORNER, WALL_SQUARE_CORNER -> when (rotation) {
                WEST -> {
                    collider.changeCollision(coordinates, WALL_NORTH_WEST, add)
                    collider.changeCollision(coordinates.transform(-1, 1), WALL_SOUTH_EAST, add)
                }
                NORTH -> {
                    collider.changeCollision(coordinates, WALL_NORTH_EAST, add)
                    collider.changeCollision(coordinates.transform(1, 1), WALL_SOUTH_WEST, add)
                }
                EAST -> {
                    collider.changeCollision(coordinates, WALL_SOUTH_EAST, add)
                    collider.changeCollision(coordinates.transform(1, -1), WALL_NORTH_WEST, add)
                }
                SOUTH -> {
                    collider.changeCollision(coordinates, WALL_SOUTH_WEST, add)
                    collider.changeCollision(coordinates.transform(-1, -1), WALL_NORTH_EAST, add)
                }
            }
            WALL_L -> when (rotation) {
                WEST -> {
                    collider.changeCollision(coordinates, WALL_NORTH or WALL_WEST, add)
                    collider.changeCollision(coordinates.transform(-1, 0), WALL_EAST, add)
                    collider.changeCollision(coordinates.transform(0, 1), WALL_SOUTH, add)
                }
                NORTH -> {
                    collider.changeCollision(coordinates, WALL_NORTH or WALL_EAST, add)
                    collider.changeCollision(coordinates.transform(0, 1), WALL_SOUTH, add)
                    collider.changeCollision(coordinates.transform(1, 0), WALL_WEST, add)
                }
                EAST -> {
                    collider.changeCollision(coordinates, WALL_SOUTH or WALL_EAST, add)
                    collider.changeCollision(coordinates.transform(1, 0), WALL_WEST, add)
                    collider.changeCollision(coordinates.transform(0, -1), WALL_NORTH, add)
                }
                SOUTH -> {
                    collider.changeCollision(coordinates, WALL_SOUTH or WALL_WEST, add)
                    collider.changeCollision(coordinates.transform(0, -1), WALL_NORTH, add)
                    collider.changeCollision(coordinates.transform(-1, 0), WALL_EAST, add)
                }
            }
        }
    }
}
