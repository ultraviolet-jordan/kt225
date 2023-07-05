package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.ZoneFlags
import org.rsmod.pathfinder.flag.CollisionFlag.OBJECT
import org.rsmod.pathfinder.flag.CollisionFlag.OBJECT_PROJECTILE_BLOCKER

/**
 * @author Jordan Abraham
 */
class LocCollider(
    private val flags: ZoneFlags
) {
    fun change(coordinates: Coordinates, width: Int, length: Int, blockproj: Boolean, add: Boolean) {
        var mask = OBJECT
        if (blockproj) {
            mask = mask or OBJECT_PROJECTILE_BLOCKER
        }
        repeat(width * length) {
            flags.change(add, coordinates.transform(it % width, it / width), mask)
        }
    }
}
