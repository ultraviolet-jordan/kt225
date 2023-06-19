package kt225.packet.builder.animator

import kt225.common.game.entity.animator.AnimatorBlockBuilder
import kt225.common.game.entity.animator.type.Teleport
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class PlayerRenderAnimatorBlockBuilder : AnimatorBlockBuilder<Teleport>(
    index = 0
) {
    override fun buildAnimatorBlock(buffer: ByteBuffer, animator: Teleport) {}
}
