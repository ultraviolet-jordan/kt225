package kt225.common.game

import kt225.common.game.entity.Entity
import kt225.common.game.entity.render.HighDefinitionRenderBlock
import kt225.common.game.entity.render.LowDefinitionRenderBlock
import kt225.common.game.entity.render.RenderBlock

/**
 * @author Jordan Abraham
 */
abstract class SynchronizerEntityRenderer<E : Entity>(
    val highDefinitionRenders: Array<ByteArray?>,
    val lowDefinitionRenders: Array<ByteArray?>
) {
    abstract fun renderEntity(entity: E)
    abstract fun clear()

    fun Array<out RenderBlock<*>?>.calculateMask(comparator: Int): Int = fold(0) { mask, block ->
        if (block == null) mask else mask or block.builder().mask
    }.let { if (it > 256) it or comparator else it }

    fun Array<out RenderBlock<*>?>.calculateSize(mask: Int): Int = fold(0) { size, block ->
        if (block == null) return@fold size
        when (block) {
            is LowDefinitionRenderBlock -> size + block.persistedBytes.size
            is HighDefinitionRenderBlock -> size + block.builder().bytesLength(block.renderType())
            else -> throw AssertionError("Block is not in correct instance.")
        }
    }.let { if (mask > 256) it + 2 else it + 1 }
}
