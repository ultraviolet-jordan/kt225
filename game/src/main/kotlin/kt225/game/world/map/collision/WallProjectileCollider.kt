package kt225.game.world.map.collision

import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocShape
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
    fun change(location: Coordinates, rotation: MapSquareLocRotation, shape: MapSquareLocShape, add: Boolean) {
        when (shape) {
            MapSquareLocShape.WALL_STRAIGHT -> when (rotation) {
                MapSquareLocRotation.WEST -> {
                    collider.changeCollision(location, WALL_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                }
                MapSquareLocRotation.NORTH -> {
                    collider.changeCollision(location, WALL_NORTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                }
                MapSquareLocRotation.EAST -> {
                    collider.changeCollision(location, WALL_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                }
                MapSquareLocRotation.SOUTH -> {
                    collider.changeCollision(location, WALL_SOUTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(0, -1), WALL_NORTH_PROJECTILE_BLOCKER, add)
                }
            }
            MapSquareLocShape.WALL_DIAGONAL_CORNER, MapSquareLocShape.WALL_SQUARE_CORNER -> when (rotation) {
                MapSquareLocRotation.WEST -> {
                    collider.changeCollision(location, WALL_NORTH_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(-1, 1), WALL_SOUTH_EAST_PROJECTILE_BLOCKER, add)
                }
                MapSquareLocRotation.NORTH -> {
                    collider.changeCollision(location, WALL_NORTH_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(1, 1), WALL_SOUTH_WEST_PROJECTILE_BLOCKER, add)
                }
                MapSquareLocRotation.EAST -> {
                    collider.changeCollision(location, WALL_SOUTH_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(1, -1), WALL_NORTH_WEST_PROJECTILE_BLOCKER, add)
                }
                MapSquareLocRotation.SOUTH -> {
                    collider.changeCollision(location, WALL_SOUTH_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(-1, -1), WALL_NORTH_EAST_PROJECTILE_BLOCKER, add)
                }
            }
            MapSquareLocShape.WALL_L -> when (rotation) {
                MapSquareLocRotation.WEST -> {
                    collider.changeCollision(location, WALL_WEST_PROJECTILE_BLOCKER or WALL_NORTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                }
                MapSquareLocRotation.NORTH -> {
                    collider.changeCollision(location, WALL_NORTH_PROJECTILE_BLOCKER or WALL_EAST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(0, 1), WALL_SOUTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                }
                MapSquareLocRotation.EAST -> {
                    collider.changeCollision(location, WALL_EAST_PROJECTILE_BLOCKER or WALL_SOUTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(1, 0), WALL_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(0, -1), WALL_NORTH_PROJECTILE_BLOCKER, add)
                }
                MapSquareLocRotation.SOUTH -> {
                    collider.changeCollision(location, WALL_SOUTH_PROJECTILE_BLOCKER or WALL_WEST_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(0, -1), WALL_NORTH_PROJECTILE_BLOCKER, add)
                    collider.changeCollision(location.transform(-1, 0), WALL_EAST_PROJECTILE_BLOCKER, add)
                }
            }
        }
    }
}
