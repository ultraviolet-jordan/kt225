package kt225.common.game.entity.player

import kt225.common.game.Client
import kt225.common.game.entity.Entity
import kt225.common.game.entity.animator.Animator
import kt225.common.game.entity.render.Renderer
import kt225.common.game.world.Coordinates
import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
abstract class Player(
    val username: String,
    val client: Client,
    world: World,
    val renderer: Renderer,
    val animator: Animator
) : Entity(world) {
    val viewport = Viewport()

    var online = false

    open fun init(coordinates: Coordinates) {
        this.coordinates = coordinates
        this.lastCoordinates = coordinates
        this.sceneCoordinates = coordinates
    }
}
