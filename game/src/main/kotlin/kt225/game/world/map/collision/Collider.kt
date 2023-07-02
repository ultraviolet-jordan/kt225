package kt225.game.world.map.collision

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
class Collider(
    private val zoneFlags: ZoneFlags
) {
    fun changeCollision(coordinates: Coordinates, mask: Int, add: Boolean) {
        if (add) {
            zoneFlags.add(coordinates.x, coordinates.z, coordinates.plane, mask)
        } else {
            zoneFlags.remove(coordinates.x, coordinates.z, coordinates.plane, mask)
        }
    }
}
