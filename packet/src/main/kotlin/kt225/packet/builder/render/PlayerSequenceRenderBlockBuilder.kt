package kt225.packet.builder.render

import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.game.entity.render.RenderBlockBuilder
import kt225.common.game.entity.render.type.Sequence
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class PlayerSequenceRenderBlockBuilder : RenderBlockBuilder<Sequence>(
    index = 1,
    mask = 0x2
) {
    override fun buildRenderBlock(buffer: ByteBuffer, render: Sequence) {
        buffer.p2(render.id)
        buffer.p1(render.delay)
    }

    override fun bytesLength(render: Sequence): Int = 3
}
