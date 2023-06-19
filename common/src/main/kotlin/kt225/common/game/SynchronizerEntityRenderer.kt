package kt225.common.game

import kt225.common.game.entity.Entity
import kt225.common.game.entity.animator.AnimatedBlock
import kt225.common.game.entity.render.HighDefinitionRenderBlock
import kt225.common.game.entity.render.LowDefinitionRenderBlock
import kt225.common.game.entity.render.RenderBlock

/**
 * @author Jordan Abraham
 */
abstract class SynchronizerEntityRenderer<E : Entity>(
    val highDefinitionRenders: Array<ByteArray?>,
    val lowDefinitionRenders: Array<ByteArray?>,
    val animatorRenders: Array<AnimatedBlock<*>?>
) {
    abstract fun renderEntity(entity: E)
    abstract fun clear()

    fun Array<out RenderBlock<*>?>.calculateMask(comparator: Int): Int {
        var mask = 0
        for (block in this) {
            if (block == null) {
                continue
            }
            mask = mask or block.builder.mask
        }
        return if (mask >= 256) mask or comparator else mask
    }

    fun Array<out RenderBlock<*>?>.calculateSize(mask: Int): Int {
        var size = 0
        for (block in this) {
            if (block == null) {
                continue
            }
            size += when (block) {
                is LowDefinitionRenderBlock -> block.persistedBytes.size
                is HighDefinitionRenderBlock -> block.builder.bytesLength(block.renderType)
                else -> throw AssertionError("Block is not in correct instance.")
            }
        }
        return if (mask >= 256) size + 2 else size + 1
    }
}
