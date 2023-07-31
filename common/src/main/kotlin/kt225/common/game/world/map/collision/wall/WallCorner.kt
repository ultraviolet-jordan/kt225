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
private inline val Boolean.northWest: Int
    get() = if (this) WALL_NORTH_WEST_PROJECTILE_BLOCKER else WALL_NORTH_WEST

private inline val Boolean.southEast: Int
    get() = if (this) WALL_SOUTH_EAST_PROJECTILE_BLOCKER else WALL_SOUTH_EAST

private inline val Boolean.northEast: Int
    get() = if (this) WALL_NORTH_EAST_PROJECTILE_BLOCKER else WALL_NORTH_EAST

private inline val Boolean.southWest: Int
    get() = if (this) WALL_SOUTH_WEST_PROJECTILE_BLOCKER else WALL_SOUTH_WEST

tailrec fun ZoneFlags.changeWallCorner(coordinates: Coordinates, rotation: MapSquareLocRotation, blockproj: Boolean, add: Boolean) {
    when (rotation) {
        WEST -> {
            change(add, coordinates, blockproj.northWest)
            change(add, coordinates.transform(-1, 1), blockproj.southEast)
        }
        NORTH -> {
            change(add, coordinates, blockproj.northEast)
            change(add, coordinates.transform(1, 1), blockproj.southWest)
        }
        EAST -> {
            change(add, coordinates, blockproj.southEast)
            change(add, coordinates.transform(1, -1), blockproj.northWest)
        }
        SOUTH -> {
            change(add, coordinates, blockproj.southWest)
            change(add, coordinates.transform(-1, -1), blockproj.northEast)
        }
    }
    if (blockproj) {
        // If we just blocked projectiles, then block normally next.
        return changeWallCorner(coordinates, rotation, false, add)
    }
}
