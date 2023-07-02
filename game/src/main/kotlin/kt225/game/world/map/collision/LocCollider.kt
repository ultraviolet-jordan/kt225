package kt225.game.world.map.collision

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
        val flag = OBJECT.let { 
            if (blockrange) it or OBJECT_PROJECTILE_BLOCKER else it
        }
        repeat(width) { x ->
            repeat(length) { z ->
                collider.changeCollision(location.transform(x, z), flag, add)
            }
        }
    }
}
