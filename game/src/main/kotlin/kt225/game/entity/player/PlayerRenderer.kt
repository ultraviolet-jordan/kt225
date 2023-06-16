package kt225.game.entity.player

import kt225.common.game.entity.render.HighDefinitionRenderBlock
import kt225.common.game.entity.render.LowDefinitionRenderBlock
import kt225.common.game.entity.render.RenderType
import kt225.common.game.entity.render.Renderer
import kt225.common.game.entity.render.type.Appearance
import kt225.common.game.entity.render.type.Sequence
import kt225.packet.builder.render.PlayerAppearanceRenderBlockBuilder
import kt225.packet.builder.render.PlayerSequenceRenderBlockBuilder

/**
 * @author Jordan Abraham
 */
class PlayerRenderer : Renderer(
    lowDefinitionRenderBlocks = arrayOfNulls(9),
    highDefinitionRenderBlocks = arrayOfNulls(9)
) {
    override fun <R : RenderType> render(type: R): R? {
        val builder = when (type) {
            is Appearance -> PlayerAppearanceRenderBlockBuilder()
            is Sequence -> PlayerSequenceRenderBlockBuilder()
            else -> null
        } ?: return null
        highDefinitionRenderBlocks[builder.index] = HighDefinitionRenderBlock(type, builder)
        return type
    }

    override fun <R : RenderType> capture(block: HighDefinitionRenderBlock<R>, bytes: ByteArray) {
        lowDefinitionRenderBlocks[block.builder.mask] = LowDefinitionRenderBlock(block.renderType, block.builder, bytes)
    }

    override fun needsRendering(): Boolean = highDefinitionRenderBlocks.any { it != null }

    override fun clear() {
        highDefinitionRenderBlocks.fill(null)
        lowDefinitionRenderBlocks.fill(null)
    }
}
