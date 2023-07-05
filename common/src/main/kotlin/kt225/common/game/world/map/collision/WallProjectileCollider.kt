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
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_WEST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_WEST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER

/**
 * @author Jordan Abraham
 */
class WallProjectileCollider(
    private val collider: Collider
) {
    fun change(coordinates: Coordinates, rotation: MapSquareLocRotation, shape: MapSquareLocShape, add: Boolean) {
        when (shape) {
            WALL_STRAIGHT -> when (rotation) {
                WEST -> {
                    collider.changeCollision(coordinates, WALL_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                }
                NORTH -> {
                    collider.changeCollision(coordinates, WALL_NORTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                }
                EAST -> {
                    collider.changeCollision(coordinates, WALL_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                }
                SOUTH -> {
                    collider.changeCollision(coordinates, WALL_SOUTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(0, -1), WALL_NORTH_PROJECTILE_BLOCKER, add)
                }
            }
            WALL_DIAGONAL_CORNER, WALL_SQUARE_CORNER -> when (rotation) {
                WEST -> {
                    collider.changeCollision(coordinates, WALL_NORTH_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(-1, 1), WALL_SOUTH_EAST_PROJECTILE_BLOCKER, add)
                }
                NORTH -> {
                    collider.changeCollision(coordinates, WALL_NORTH_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(1, 1), WALL_SOUTH_WEST_PROJECTILE_BLOCKER, add)
                }
                EAST -> {
                    collider.changeCollision(coordinates, WALL_SOUTH_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(1, -1), WALL_NORTH_WEST_PROJECTILE_BLOCKER, add)
                }
                SOUTH -> {
                    collider.changeCollision(coordinates, WALL_SOUTH_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(-1, -1), WALL_NORTH_EAST_PROJECTILE_BLOCKER, add)
                }
            }
            WALL_L -> when (rotation) {
                WEST -> {
                    collider.changeCollision(coordinates, WALL_WEST_PROJECTILE_BLOCKER or WALL_NORTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                }
                NORTH -> {
                    collider.changeCollision(coordinates, WALL_NORTH_PROJECTILE_BLOCKER or WALL_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                }
                EAST -> {
                    collider.changeCollision(coordinates, WALL_EAST_PROJECTILE_BLOCKER or WALL_SOUTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(0, -1), WALL_NORTH_PROJECTILE_BLOCKER, add)
                }
                SOUTH -> {
                    collider.changeCollision(coordinates, WALL_SOUTH_PROJECTILE_BLOCKER or WALL_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(0, -1), WALL_NORTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(coordinates.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                }
            }
        }
    }
}
