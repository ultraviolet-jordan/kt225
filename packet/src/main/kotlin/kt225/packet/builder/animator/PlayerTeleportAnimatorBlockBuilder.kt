package kt225.packet.builder.animator

import kt225.common.buffer.BitAccess
import kt225.common.game.entity.animator.AnimatorBlockBuilder
import kt225.common.game.entity.animator.type.Teleport

/**
 * @author Jordan Abraham
 */
class PlayerTeleportAnimatorBlockBuilder : AnimatorBlockBuilder<Teleport>(
    index = 3
) {
    override fun buildAnimatorBlock(buffer: BitAccess, animator: Teleport) {
        buffer.pBit(2, animator.plane)
        buffer.pBit(7, animator.x)
        buffer.pBit(7, animator.z)
        buffer.pBit(1, 1) // Clear movement queue.
        buffer.pBit(1, if (animator.rendering) 1 else 0)
    }
}
