package kt225.common.game.entity.route

import kt225.common.game.entity.EntityDirection

/**
 * @author Jordan Abraham
 */
data class RouteStepDirection(
    val walkDirection: EntityDirection,
    val runDirection: EntityDirection
)
