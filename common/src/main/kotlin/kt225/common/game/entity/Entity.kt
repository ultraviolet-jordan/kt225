package kt225.common.game.entity

import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
abstract class Entity(
    val world: World
) {
    abstract fun login()
}
