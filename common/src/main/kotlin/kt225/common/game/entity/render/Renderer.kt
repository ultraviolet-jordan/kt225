package kt225.common.game.entity.render

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
abstract class Renderer(
    val lowDefinitionRenderBlocks: Array<LowDefinitionRenderBlock<*>?>,
    val highDefinitionRenderBlocks: Array<HighDefinitionRenderBlock<*>?>
) {
    val highDefinitionRendering: Boolean get() = highDefinitionRenderBlocks.any { it != null }
    val lowDefinitionRendering: Boolean get() = highDefinitionRenderBlocks.any { it != null }
    
    abstract fun <R : RenderType> render(type: R): R?

    fun <R : RenderType> capture(block: HighDefinitionRenderBlock<R>, slice: ByteBuffer) {
        lowDefinitionRenderBlocks[block.builder.mask] = LowDefinitionRenderBlock(block.renderType, block.builder, slice)
    }

    fun clear() {
        highDefinitionRenderBlocks.fill(null)
        lowDefinitionRenderBlocks.fill(null)
    }
}
