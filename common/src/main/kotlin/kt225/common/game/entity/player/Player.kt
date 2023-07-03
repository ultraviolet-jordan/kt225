package kt225.common.game.entity.player

import kt225.common.game.Client
import kt225.common.game.entity.Entity
import kt225.common.game.entity.EntityDirection
import kt225.common.game.entity.animator.Animator
import kt225.common.game.entity.animator.type.Run
import kt225.common.game.entity.animator.type.Walk
import kt225.common.game.entity.render.Renderer
import kt225.common.game.entity.route.RouteStepDirection
import kt225.common.game.world.Coordinates
import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
abstract class Player(
    world: World,
    val username: String,
    val client: Client,
    val renderer: Renderer,
    val animator: Animator
) : Entity(world) {
    val viewport = Viewport()

    var online = false

    open fun init(coordinates: Coordinates) {
        this.coordinates = coordinates
        this.lastCoordinates = coordinates
    }

    override fun moveTo(coordinates: Coordinates, stepDirection: RouteStepDirection) {
        this.coordinates = coordinates
        
        val rendering = renderer.highDefinitionRendering
        val walkDirection = stepDirection.walkDirection
        val runDirection = stepDirection.runDirection
        if (runDirection != EntityDirection.NONE.id) {
            animator.animate(Run(rendering, walkDirection, runDirection))
        } else {
            animator.animate(Walk(rendering, walkDirection))
        }
    }
}
