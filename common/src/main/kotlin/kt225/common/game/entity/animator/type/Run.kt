package kt225.common.game.entity.animator.type

import kt225.common.game.entity.EntityDirection
import kt225.common.game.entity.animator.AnimatorType

/**
 * @author Jordan Abraham
 */
data class Run(
    val rendering: Boolean,
    val walkDirection: EntityDirection,
    val runDirection: EntityDirection
) : AnimatorType
