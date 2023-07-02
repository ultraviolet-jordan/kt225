package kt225.game.world.map.collision

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.flag.CollisionFlag

/**
 * @author Jordan Abraham
 */
class FloorCollider(
    private val collider: Collider
) {
    fun change(coordinates: Coordinates, add: Boolean) {
        collider.changeCollision(coordinates, CollisionFlag.FLOOR, add)
    }
}
