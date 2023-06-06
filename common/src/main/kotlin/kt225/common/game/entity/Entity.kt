package kt225.common.game.entity

import kt225.common.game.entity.render.Renderer
import kt225.common.game.world.Position
import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
abstract class Entity(
    val world: World
) {
    var index = 0

    var position = Position.None
        protected set

    var lastPosition = Position.None
        protected set

    var scenePosition = Position.None
        protected set

    abstract fun login()
    abstract fun renderer(): Renderer
}
