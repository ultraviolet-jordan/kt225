package kt225.common.game.entity.render

/**
 * @author Jordan Abraham
 */
class HighDefinitionRenderBlock<R : RenderType>(
    renderType: R,
    builder: RenderBlockBuilder<R>,
) : RenderBlock<R>(renderType, builder)
