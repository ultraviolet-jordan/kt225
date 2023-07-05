package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates

/**
 * @author Jordan Abraham
 */
class OpenCollider(
    private val collider: Collider
) {
    fun open(coordinates: Coordinates) {
        collider.setCollision(coordinates, 0x0)
    }
}