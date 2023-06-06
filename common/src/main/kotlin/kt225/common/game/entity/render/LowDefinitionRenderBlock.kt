package kt225.common.game.entity.render

/**
 * @author Jordan Abraham
 */
class LowDefinitionRenderBlock<R : RenderType>(
    renderType: R,
    builder: RenderBlockBuilder<R>,
    val persistedBytes: ByteArray
) : RenderBlock<R>(renderType, builder)
