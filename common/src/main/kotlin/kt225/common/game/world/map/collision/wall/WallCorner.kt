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
    val northWest = if (blockproj) WALL_NORTH_WEST_PROJECTILE_BLOCKER else WALL_NORTH_WEST
    val southEast = if (blockproj) WALL_SOUTH_EAST_PROJECTILE_BLOCKER else WALL_SOUTH_EAST
    val northEast = if (blockproj) WALL_NORTH_EAST_PROJECTILE_BLOCKER else WALL_NORTH_EAST
    val southWest = if (blockproj) WALL_SOUTH_WEST_PROJECTILE_BLOCKER else WALL_SOUTH_WEST

    when (rotation) {
        WEST -> {
            change(add, coordinates, northWest)
            change(add, coordinates.transform(-1, 1), southEast)
        }
        NORTH -> {
            change(add, coordinates, northEast)
            change(add, coordinates.transform(1, 1), southWest)
        }
        EAST -> {
            change(add, coordinates, southEast)
            change(add, coordinates.transform(1, -1), northWest)
        }
        SOUTH -> {
            change(add, coordinates, southWest)
            change(add, coordinates.transform(-1, -1), northEast)
        }
    }

    if (blockproj) {
        // If we just blocked projectiles, then block normally next.
        return changeWallCorner(coordinates, rotation, false, add)
    }
}
