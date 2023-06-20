package kt225.game

import com.google.inject.Singleton
import kt225.common.buffer.ip2
import kt225.common.buffer.p1
import kt225.common.game.SynchronizerEntityRenderer
import kt225.common.game.entity.player.Player
import kt225.common.game.entity.render.HighDefinitionRenderBlock
import kt225.common.game.entity.render.LowDefinitionRenderBlock
import kt225.common.game.entity.render.RenderBlock
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class PlayerSynchronizerRenderer : SynchronizerEntityRenderer<Player>(
    highDefinitionRenders = arrayOfNulls(2048),
    lowDefinitionRenders = arrayOfNulls(2048),
    animatorRenders = arrayOfNulls(2048)
) {
    override fun renderEntity(entity: Player) {
        val renderer = entity.renderer
        if (renderer.highDefinitionRendering) {
            highDefinitionRenders[entity.index] = buildHighDefinitionUpdates(entity, renderer.highDefinitionRenderBlocks)
        }
        if (renderer.lowDefinitionRendering) {
            lowDefinitionRenders[entity.index] = buildLowDefinitionUpdates(renderer.lowDefinitionRenderBlocks)
        }
        entity.animator.block?.let { animatorRenders[entity.index] = it }
    }

    private fun buildHighDefinitionUpdates(player: Player, blocks: Array<HighDefinitionRenderBlock<*>?>): ByteArray {
        val buffer = blocks.allocBlocksBuffer()
        for (block in blocks) {
            if (block == null) {
                continue
            }
            val start = buffer.position()
            block.builder.buildRenderBlock(buffer, block.renderType)
            val end = buffer.position()
            player.renderer.capture(block, buffer.slice(start, end - start))
        }
        return buffer.array()
    }

    private fun buildLowDefinitionUpdates(blocks: Array<LowDefinitionRenderBlock<*>?>): ByteArray {
        val buffer = blocks.allocBlocksBuffer()
        for (block in blocks) {
            if (block == null) {
                continue
            }
            buffer.put(block.persisted)
        }
        return buffer.array()
    }
    
    private inline fun <reified T : RenderBlock<*>> Array<T?>.allocBlocksBuffer(): ByteBuffer {
        val mask = calculateMask(0x80)
        val size = calculateSize(mask)
        val buffer = ByteBuffer.allocate(size)
        if (mask >= 256) {
            buffer.ip2(mask)
        } else {
            buffer.p1(mask)
        }
        return buffer
    }
}
