package kt225.common.game.world.map.collision.wall

import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocRotation.Companion.EAST
import kt225.common.game.world.map.MapSquareLocRotation.Companion.NORTH
import kt225.common.game.world.map.MapSquareLocRotation.Companion.SOUTH
import kt225.common.game.world.map.MapSquareLocRotation.Companion.WEST
import kt225.common.game.world.map.collision.change
import org.rsmod.pathfinder.ZoneFlags
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER

/**
 * @author Jordan Abraham
 */
tailrec fun ZoneFlags.changeWallStraight(coordinates: Coordinates, rotation: MapSquareLocRotation, blockproj: Boolean, add: Boolean) {
    when (rotation) {
        WEST -> {
            val west = coordinates.transform(-1, 0)
            if (blockproj) {
                change(add, coordinates, WALL_WEST_PROJECTILE_BLOCKER)
                change(add, west, WALL_EAST_PROJECTILE_BLOCKER)
            } else {
                change(add, coordinates, WALL_WEST)
                change(add, west, WALL_EAST)
            }
        }
        NORTH -> {
            val north = coordinates.transform(0, 1)
            if (blockproj) {
                change(add, coordinates, WALL_NORTH_PROJECTILE_BLOCKER)
                change(add, north, WALL_SOUTH_PROJECTILE_BLOCKER)
            } else {
                change(add, coordinates, WALL_NORTH)
                change(add, north, WALL_SOUTH)
            }
        }
        EAST -> {
            val east = coordinates.transform(1, 0)
            if (blockproj) {
                change(add, coordinates, WALL_EAST_PROJECTILE_BLOCKER)
                change(add, east, WALL_WEST_PROJECTILE_BLOCKER)
            } else {
                change(add, coordinates, WALL_EAST)
                change(add, east, WALL_WEST)
            }
        }
        SOUTH -> {
            val south = coordinates.transform(0, -1)
            if (blockproj) {
                change(add, coordinates, WALL_SOUTH_PROJECTILE_BLOCKER)
                change(add, south, WALL_NORTH_PROJECTILE_BLOCKER)
            } else {
                change(add, coordinates, WALL_SOUTH)
                change(add, south, WALL_NORTH)
            }
        }
    }
    if (blockproj) {
        // If we just blocked projectiles, then block normally next.
        return changeWallStraight(coordinates, rotation, false, add)
    }
}
