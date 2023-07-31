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

private inline val Boolean.west: Int
    get() = if (this) WALL_WEST_PROJECTILE_BLOCKER else WALL_WEST

private inline val Boolean.north: Int
    get() = if (this) WALL_NORTH_PROJECTILE_BLOCKER else WALL_NORTH

private inline val Boolean.east: Int
    get() = if (this) WALL_EAST_PROJECTILE_BLOCKER else WALL_EAST

private inline val Boolean.south: Int
    get() = if (this) WALL_SOUTH_PROJECTILE_BLOCKER else WALL_SOUTH

/**
 * @author Jordan Abraham
 */
tailrec fun ZoneFlags.changeWallStraight(coordinates: Coordinates, rotation: MapSquareLocRotation, blockproj: Boolean, add: Boolean) {
    when (rotation) {
        WEST -> {
            change(add, coordinates, blockproj.west)
            change(add, coordinates.transform(-1, 0), blockproj.east)
        }
        NORTH -> {
            change(add, coordinates, blockproj.north)
            change(add, coordinates.transform(0, 1), blockproj.south)
        }
        EAST -> {
            change(add, coordinates, blockproj.east)
            change(add, coordinates.transform(1, 0), blockproj.west)
        }
        SOUTH -> {
            change(add, coordinates, blockproj.south)
            change(add, coordinates.transform(0, -1), blockproj.north)
        }
    }
    if (blockproj) {
        // If we just blocked projectiles, then block normally next.
        return changeWallStraight(coordinates, rotation, false, add)
    }
}
