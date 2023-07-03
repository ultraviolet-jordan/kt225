package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.flag.CollisionFlag.FLOOR

/**
 * @author Jordan Abraham
 */
class FloorCollider(
    private val collider: Collider
) {
    fun change(coordinates: Coordinates, add: Boolean) {
        collider.changeCollision(coordinates, FLOOR, add)
    }
}
