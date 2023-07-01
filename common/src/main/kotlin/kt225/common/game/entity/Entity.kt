package kt225.common.game.entity

import kt225.common.game.world.Position
import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
abstract class Entity(
    val world: World
) {
    var index = 0

    var position = Position.NONE
        protected set

    var lastPosition = Position.NONE
        protected set

    var scenePosition = Position.NONE
        protected set

    var mapSquareChanged = true
        protected set

    abstract fun login()

    fun reset() {
        mapSquareChanged = false
    }
}
