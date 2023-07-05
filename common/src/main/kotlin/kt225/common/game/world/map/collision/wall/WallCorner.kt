package kt225.common.game.world.map.collision.wall

import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocRotation.Companion.EAST
import kt225.common.game.world.map.MapSquareLocRotation.Companion.NORTH
import kt225.common.game.world.map.MapSquareLocRotation.Companion.SOUTH
import kt225.common.game.world.map.MapSquareLocRotation.Companion.WEST
import kt225.common.game.world.map.collision.change
import org.rsmod.pathfinder.ZoneFlags
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_NORTH_WEST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_EAST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_EAST_PROJECTILE_BLOCKER
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_WEST
import org.rsmod.pathfinder.flag.CollisionFlag.WALL_SOUTH_WEST_PROJECTILE_BLOCKER

/**
 * @author Jordan Abraham
 */
tailrec fun ZoneFlags.changeWallCorner(coordinates: Coordinates, rotation: MapSquareLocRotation, blockproj: Boolean, add: Boolean) {
    when (rotation) {
        WEST -> {
            val northWest = coordinates.transform(-1, 1)
            if (blockproj) {
                change(add, coordinates, WALL_NORTH_WEST_PROJECTILE_BLOCKER)
                change(add, northWest, WALL_SOUTH_EAST_PROJECTILE_BLOCKER)
            } else {
                change(add, coordinates, WALL_NORTH_WEST)
                change(add, northWest, WALL_SOUTH_EAST)
            }
        }
        NORTH -> {
            val northEast = coordinates.transform(1, 1)
            if (blockproj) {
                change(add, coordinates, WALL_NORTH_EAST_PROJECTILE_BLOCKER)
                change(add, northEast, WALL_SOUTH_WEST_PROJECTILE_BLOCKER)
            } else {
                change(add, coordinates, WALL_NORTH_EAST)
                change(add, northEast, WALL_SOUTH_WEST)
            }
        }
        EAST -> {
            val southEast = coordinates.transform(1, -1)
            if (blockproj) {
                change(add, coordinates, WALL_SOUTH_EAST_PROJECTILE_BLOCKER)
                change(add, southEast, WALL_NORTH_WEST_PROJECTILE_BLOCKER)
            } else {
                change(add, coordinates, WALL_SOUTH_EAST)
                change(add, southEast, WALL_NORTH_WEST)
            }
        }
        SOUTH -> {
            val southWest = coordinates.transform(-1, -1)
            if (blockproj) {
                change(add, coordinates, WALL_SOUTH_WEST_PROJECTILE_BLOCKER)
                change(add, southWest, WALL_NORTH_EAST_PROJECTILE_BLOCKER)
            } else {
                change(add, coordinates, WALL_SOUTH_WEST)
                change(add, southWest, WALL_NORTH_EAST)
            }
        }
    }
    if (blockproj) {
        // If we just blocked projectiles, then block normally next.
        return changeWallCorner(coordinates, rotation, false, add)
    }
}
