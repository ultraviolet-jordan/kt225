package kt225.game.entity.player

import kt225.common.game.entity.animator.AnimatedBlock
import kt225.common.game.entity.animator.Animator
import kt225.common.game.entity.animator.AnimatorType

/**
 * @author Jordan Abraham
 */
class PlayerAnimator : Animator() {
    override fun <A : AnimatorType> animate(type: A): A? {
        val builder = PlayerAnimatorBlockBuilders[type::class] ?: return null
        this.block = AnimatedBlock(type, builder)
        return type
    }
}
