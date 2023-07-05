package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.ZoneFlags
import org.rsmod.pathfinder.flag.CollisionFlag.FLOOR

/**
 * @author Jordan Abraham
 */
class FloorCollider(
    private val flags: ZoneFlags
) {
    fun change(coordinates: Coordinates, add: Boolean) {
        flags.change(add, coordinates, FLOOR)
    }
}
