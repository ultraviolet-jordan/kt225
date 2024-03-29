package kt225.packet.builder.animator

import kt225.common.buffer.pbit
import kt225.common.game.entity.animator.AnimatorBlockBuilder
import kt225.common.game.entity.animator.type.Walk
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class PlayerWalkAnimatorBlockBuilder : AnimatorBlockBuilder<Walk>(
    index = 1
) {
    override fun buildAnimatorBlock(buffer: ByteBuffer, animator: Walk) {
        buffer.pbit(3, animator.direction)
        buffer.pbit(1, animator.rendering)
    }
}
