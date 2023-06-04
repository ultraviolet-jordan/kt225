package kt225.common.game.entity.player

import kt225.common.game.Client
import kt225.common.game.entity.Entity
import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
abstract class Player(
    val client: Client,
    world: World
) : Entity(world) {
    abstract fun init()
}
