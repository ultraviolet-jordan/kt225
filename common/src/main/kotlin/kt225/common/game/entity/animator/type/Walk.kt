package kt225.common.game.entity.animator.type

import kt225.common.game.entity.EntityDirection
import kt225.common.game.entity.animator.AnimatorType

/**
 * @author Jordan Abraham
 */
data class Walk(
    val rendering: Boolean,
    val direction: EntityDirection
) : AnimatorType
