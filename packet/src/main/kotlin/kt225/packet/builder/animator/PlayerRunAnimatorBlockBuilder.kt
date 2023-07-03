package kt225.packet.builder.animator

import kt225.common.buffer.pbit
import kt225.common.game.entity.animator.AnimatorBlockBuilder
import kt225.common.game.entity.animator.type.Run
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class PlayerRunAnimatorBlockBuilder : AnimatorBlockBuilder<Run>(
    index = 2
) {
    override fun buildAnimatorBlock(buffer: ByteBuffer, animator: Run) {
        buffer.pbit(3, animator.walkDirection)
        buffer.pbit(3, animator.runDirection)
        buffer.pbit(1, animator.rendering)
    }
}
