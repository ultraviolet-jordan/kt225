package kt225.packet.builder.animator

import kt225.common.buffer.pbit
import kt225.common.game.entity.animator.AnimatorBlockBuilder
import kt225.common.game.entity.animator.type.Teleport
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class PlayerTeleportAnimatorBlockBuilder : AnimatorBlockBuilder<Teleport>(
    index = 3
) {
    override fun buildAnimatorBlock(buffer: ByteBuffer, animator: Teleport) {
        buffer.pbit(2, animator.plane)
        buffer.pbit(7, animator.x)
        buffer.pbit(7, animator.z)
        buffer.pbit(1, 1)
        buffer.pbit(1, animator.rendering)
    }
}
