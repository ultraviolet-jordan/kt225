package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.ZoneFlags
import org.rsmod.pathfinder.flag.CollisionFlag.ROOF

/**
 * @author Jordan Abraham
 */
class RoofCollider(
    private val flags: ZoneFlags
) {
    fun change(coordinates: Coordinates, add: Boolean) {
        flags.change(add, coordinates, ROOF)
    }
}
