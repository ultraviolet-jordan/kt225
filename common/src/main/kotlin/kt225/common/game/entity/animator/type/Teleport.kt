package kt225.common.game.entity.animator.type

import kt225.common.game.entity.animator.AnimatorType

/**
 * @author Jordan Abraham
 */
data class Teleport(
    val rendering: Boolean,
    val x: Int,
    val z: Int,
    val plane: Int
) : AnimatorType
