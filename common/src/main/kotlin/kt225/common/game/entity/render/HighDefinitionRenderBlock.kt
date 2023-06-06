package kt225.common.game.entity.render

/**
 * @author Jordan Abraham
 */
class HighDefinitionRenderBlock<R : RenderType>(
    private val renderType: R,
    private val builder: RenderBlockBuilder<R>,
) : RenderBlock<R>() {
    override fun renderType(): R = renderType
    override fun builder(): RenderBlockBuilder<R> = builder
}
