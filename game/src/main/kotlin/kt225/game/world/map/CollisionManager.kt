package kt225.game.world.map

import kt225.cache.map.MapSquareLands
import kt225.cache.map.MapSquareLocs
import kt225.cache225.map.MapSquareLandEntryType
import kt225.cache225.map.MapSquareLocEntryType

/**
 * @author Jordan Abraham
 */
class CollisionManager {
    fun applyCollision(lands: MapSquareLands<MapSquareLandEntryType>, locs: MapSquareLocs<MapSquareLocEntryType>) {
        for (plane in 0 until 4) {
            for (x in 0 until 64) {
                for (z in 0 until 64) {
                    // TODO Collision
                }
            }
        }
    }
}
