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
    val west = if (blockproj) WALL_WEST_PROJECTILE_BLOCKER else WALL_WEST
    val north = if (blockproj) WALL_NORTH_PROJECTILE_BLOCKER else WALL_NORTH
    val east = if (blockproj) WALL_EAST_PROJECTILE_BLOCKER else WALL_EAST
    val south = if (blockproj) WALL_SOUTH_PROJECTILE_BLOCKER else WALL_SOUTH

    when (rotation) {
        WEST -> {
            change(add, coordinates, west)
            change(add, coordinates.transform(-1, 0), east)
        }
        NORTH -> {
            change(add, coordinates, north)
            change(add, coordinates.transform(0, 1), south)
        }
        EAST -> {
            change(add, coordinates, east)
            change(add, coordinates.transform(1, 0), west)
        }
        SOUTH -> {
            change(add, coordinates, south)
            change(add, coordinates.transform(0, -1), north)
        }
    }

    if (blockproj) {
        // If we just blocked projectiles, then block normally next.
        return changeWallStraight(coordinates, rotation, false, add)
    }
}
