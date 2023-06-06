package kt225.game.entity.player

import kt225.common.game.entity.animator.AnimatedBlock
import kt225.common.game.entity.animator.Animator
import kt225.common.game.entity.animator.AnimatorType
import kt225.common.game.entity.animator.type.Render
import kt225.common.game.entity.animator.type.Teleport
import kt225.packet225.builder.animator.PlayerRenderAnimatorBlockBuilder
import kt225.packet225.builder.animator.PlayerTeleportAnimatorBlockBuilder

/**
 * @author Jordan Abraham
 */
class PlayerAnimator : Animator {

    private var animatorBlock: AnimatedBlock<*>? = null

    override fun <A : AnimatorType> animate(type: A): A? {
        val builder = when (type) {
            is Render -> PlayerRenderAnimatorBlockBuilder()
            is Teleport -> PlayerTeleportAnimatorBlockBuilder()
            else -> null
        } ?: return null
        animatorBlock = AnimatedBlock(type, builder)
        return type
    }

    override fun block(): AnimatedBlock<*>? {
        return animatorBlock
    }

    override fun clear() {
        this.animatorBlock = null
    }
}
