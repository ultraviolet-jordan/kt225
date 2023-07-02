package kt225.game.world.map.collision

import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocShape
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
    fun change(location: Coordinates, rotation: MapSquareLocRotation, shape: MapSquareLocShape, add: Boolean) {
        when (shape) {
            MapSquareLocShape.WALL_STRAIGHT -> when (rotation) {
                MapSquareLocRotation.WEST -> {
                    collider.changeCollision(location, WALL_WEST, add)
                    collider.changeCollision(location.transform(-1, 0), WALL_EAST, add)
                }
                MapSquareLocRotation.NORTH -> {
                    collider.changeCollision(location, WALL_NORTH, add)
                    collider.changeCollision(location.transform(0, 1), WALL_SOUTH, add)
                }
                MapSquareLocRotation.EAST -> {
                    collider.changeCollision(location, WALL_EAST, add)
                    collider.changeCollision(location.transform(1, 0), WALL_WEST, add)
                }
                MapSquareLocRotation.SOUTH -> {
                    collider.changeCollision(location, WALL_SOUTH, add)
                    collider.changeCollision(location.transform(0, -1), WALL_NORTH, add)
                }
            }
            MapSquareLocShape.WALL_DIAGONAL_CORNER, MapSquareLocShape.WALL_SQUARE_CORNER -> when (rotation) {
                MapSquareLocRotation.WEST -> {
                    collider.changeCollision(location, WALL_NORTH_WEST, add)
                    collider.changeCollision(location.transform(-1, 1), WALL_SOUTH_EAST, add)
                }
                MapSquareLocRotation.NORTH -> {
                    collider.changeCollision(location, WALL_NORTH_EAST, add)
                    collider.changeCollision(location.transform(1, 1), WALL_SOUTH_WEST, add)
                }
                MapSquareLocRotation.EAST -> {
                    collider.changeCollision(location, WALL_SOUTH_EAST, add)
                    collider.changeCollision(location.transform(1, -1), WALL_NORTH_WEST, add)
                }
                MapSquareLocRotation.SOUTH -> {
                    collider.changeCollision(location, WALL_SOUTH_WEST, add)
                    collider.changeCollision(location.transform(-1, -1), WALL_NORTH_EAST, add)
                }
            }
            MapSquareLocShape.WALL_L -> when (rotation) {
                MapSquareLocRotation.WEST -> {
                    collider.changeCollision(location, WALL_NORTH or WALL_WEST, add)
                    collider.changeCollision(location.transform(-1, 0), WALL_EAST, add)
                    collider.changeCollision(location.transform(0, 1), WALL_SOUTH, add)
                }
                MapSquareLocRotation.NORTH -> {
                    collider.changeCollision(location, WALL_NORTH or WALL_EAST, add)
                    collider.changeCollision(location.transform(0, 1), WALL_SOUTH, add)
                    collider.changeCollision(location.transform(1, 0), WALL_WEST, add)
                }
                MapSquareLocRotation.EAST -> {
                    collider.changeCollision(location, WALL_SOUTH or WALL_EAST, add)
                    collider.changeCollision(location.transform(1, 0), WALL_WEST, add)
                    collider.changeCollision(location.transform(0, -1), WALL_NORTH, add)
                }
                MapSquareLocRotation.SOUTH -> {
                    collider.changeCollision(location, WALL_SOUTH or WALL_WEST, add)
                    collider.changeCollision(location.transform(0, -1), WALL_NORTH, add)
                    collider.changeCollision(location.transform(-1, 0), WALL_EAST, add)
                }
            }
        }
    }
}
