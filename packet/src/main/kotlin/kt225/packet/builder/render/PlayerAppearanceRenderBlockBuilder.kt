package kt225.packet.builder.render

import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.p8
import kt225.common.game.entity.render.RenderBlockBuilder
import kt225.common.game.entity.render.type.Appearance
import kt225.common.string.toBase37
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class PlayerAppearanceRenderBlockBuilder : RenderBlockBuilder<Appearance>(
    index = 0,
    mask = 0x1
) {
    override fun buildRenderBlock(buffer: ByteBuffer, render: Appearance) {
        buffer.p1(bytesLength(render) - 1)
        buffer.p1(render.gender.id)
        buffer.p1(render.headIcon)
        repeat(5) {
            buffer.p1(0)
        }
        render.bodyParts.forEach {
            buffer.p2(0x100 + it)
        }
        render.bodyPartColors.forEach(buffer::p1)
        render.renderSequences.forEach(buffer::p2)
        buffer.p8(render.name.toBase37)
        buffer.p1(render.combatLevel)
    }

    override fun bytesLength(render: Appearance): Int = 50
}
