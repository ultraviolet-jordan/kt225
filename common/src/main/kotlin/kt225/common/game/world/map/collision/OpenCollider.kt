package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
class OpenCollider(
    private val flags: ZoneFlags
) {
    fun open(coordinates: Coordinates) {
        flags[coordinates] = 0x0
    }
}
