package kt225.packet225.builder.animator

import kt225.common.buffer.BitAccess
import kt225.common.game.entity.animator.AnimatorBlockBuilder
import kt225.common.game.entity.animator.type.Teleport

/**
 * @author Jordan Abraham
 */
class PlayerRenderAnimatorBlockBuilder : AnimatorBlockBuilder<Teleport>(
    index = 0
) {
    override fun buildAnimatorBlock(buffer: BitAccess, animator: Teleport) {}
}
