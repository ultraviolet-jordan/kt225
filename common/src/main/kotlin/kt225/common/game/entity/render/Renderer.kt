package kt225.common.game.entity.render

/**
 * @author Jordan Abraham
 */
abstract class Renderer(
    val lowDefinitionRenderBlocks: Array<LowDefinitionRenderBlock<*>?>,
    val highDefinitionRenderBlocks: Array<HighDefinitionRenderBlock<*>?>
) {
    abstract fun <R : RenderType> render(type: R): R?
    abstract fun <R : RenderType> capture(block: HighDefinitionRenderBlock<R>, bytes: ByteArray)
    abstract fun needsRendering(): Boolean
    abstract fun clear()
}
