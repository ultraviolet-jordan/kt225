package kt225.common.game.entity.player

import kt225.common.game.Client
import kt225.common.game.entity.Entity
import kt225.common.game.world.Position
import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
abstract class Player(
    val client: Client,
    world: World
) : Entity(world) {
    val viewport = Viewport()

    open fun init(position: Position) {
        this.position = position
        this.lastPosition = position
        this.scenePosition = position
    }

    abstract fun online(): Boolean
}
