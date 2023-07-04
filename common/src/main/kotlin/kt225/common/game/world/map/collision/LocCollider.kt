package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.flag.CollisionFlag.OBJECT
import org.rsmod.pathfinder.flag.CollisionFlag.OBJECT_PROJECTILE_BLOCKER

/**
 * @author Jordan Abraham
 */
class LocCollider(
    private val collider: Collider
) {
    fun change(location: Coordinates, width: Int, length: Int, blockrange: Boolean, add: Boolean) {
        var mask = OBJECT
        if (blockrange) {
            mask = mask or OBJECT_PROJECTILE_BLOCKER
        }
        repeat(width * length) {
            collider.changeCollision(location.transform(it % width, it / width), mask, add)
        }
    }
}
