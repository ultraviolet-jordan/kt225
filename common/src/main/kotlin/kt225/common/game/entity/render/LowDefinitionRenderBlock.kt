package kt225.common.game.entity.render

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class LowDefinitionRenderBlock<R : RenderType>(
    renderType: R,
    builder: RenderBlockBuilder<R>,
    val persisted: ByteBuffer
) : RenderBlock<R>(renderType, builder)
