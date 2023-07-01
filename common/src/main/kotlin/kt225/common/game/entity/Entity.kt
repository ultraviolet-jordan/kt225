package kt225.common.game.entity

import kt225.common.game.world.Coordinates
import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
abstract class Entity(
    val world: World
) {
    var index = 0

    var coordinates = Coordinates.NONE
        protected set

    var lastCoordinates = Coordinates.NONE
        protected set

    var sceneCoordinates = Coordinates.NONE
        protected set

    var mapSquareChanged = true
        protected set

    abstract fun login()

    fun reset() {
        mapSquareChanged = false
    }
}
