package kt225.game

import com.google.inject.Singleton
import kt225.common.buffer.p1
import kt225.common.buffer.pArrayBuffer
import kt225.common.game.SynchronizerEntityRenderer
import kt225.common.game.entity.player.Player
import kt225.common.game.entity.render.HighDefinitionRenderBlock
import kt225.common.game.entity.render.LowDefinitionRenderBlock
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
        val renderer = entity.renderer()
        if (renderer.needsRendering()) {
            highDefinitionRenders[entity.index] = buildHighDefinitionUpdates(entity, renderer.highDefinitionRenderBlocks)
        }
        lowDefinitionRenders[entity.index] = buildLowDefinitionUpdates(renderer.lowDefinitionRenderBlocks)
        animatorRenders[entity.index] = entity.animator().block()
    }

    override fun clear() {
        highDefinitionRenders.fill(null)
        lowDefinitionRenders.fill(null)
        animatorRenders.fill(null)
    }

    private fun buildHighDefinitionUpdates(player: Player, blocks: Array<HighDefinitionRenderBlock<*>?>): ByteArray {
        val mask = blocks.calculateMask(0x80)
        val size = blocks.calculateSize(mask)
        return ByteBuffer.allocate(size).also {
            it.p1(mask and 0xFF)
            if (mask >= 256) {
                it.p1(mask shr 8)
            }
            for (block in blocks) {
                if (block == null) {
                    continue
                }
                val start = it.position()
                block.builder.buildRenderBlock(it, block.renderType)
                val end = it.position()
                player.renderer().capture(block, it.array().sliceArray(start until end))
            }
        }.array()
    }

    private fun buildLowDefinitionUpdates(blocks: Array<LowDefinitionRenderBlock<*>?>): ByteArray {
        val mask = blocks.calculateMask(0x80)
        val size = blocks.calculateSize(mask)
        return ByteBuffer.allocate(size).also {
            it.p1(mask and 0xFF)
            if (mask >= 256) {
                it.p1(mask shr 8)
            }
            for (block in blocks) {
                if (block == null) {
                    continue
                }
                it.pArrayBuffer(block.persistedBytes)
            }
        }.array()
    }
}
