package kt225.common.game.entity.render

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
abstract class RenderBlockBuilder<out R : RenderType>(
    val index: Int,
    val mask: Int
) {
    abstract fun buildRenderBlock(buffer: ByteBuffer, render: @UnsafeVariance R)
    abstract fun bytesLength(render: @UnsafeVariance R): Int
}
