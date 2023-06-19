package kt225.packet.builder

import com.google.inject.Singleton
import kt225.common.buffer.accessBits
import kt225.common.buffer.pbit
import kt225.common.buffer.pdata
import kt225.common.game.entity.animator.Animator
import kt225.common.game.entity.player.Viewport
import kt225.common.game.entity.render.Renderer
import kt225.common.packet.PacketBuilder
import kt225.packet.type.server.PlayerInfoPacket
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class PlayerInfoPacketBuilder : PacketBuilder<PlayerInfoPacket>(
    id = 184,
    length = -2
) {
    override fun buildPacket(packet: PlayerInfoPacket, buffer: ByteBuffer) {
        val observing = packet.observing
        val viewport = observing.viewport
        buffer.accessBits {
            updateLocalPlayer(observing.index, viewport, observing.renderer, observing.animator)
            updateOtherPlayers(viewport)
            updateNewPlayers(viewport)
        }
        buffer.updatePlayerMasks(viewport, packet)
    }

    private fun ByteBuffer.updateLocalPlayer(index: Int, viewport: Viewport, renderer: Renderer, animator: Animator) {
        val rendering = renderer.needsRendering()
        val animatedBlock = animator.block()
        if (animatedBlock == null) {
            pbit(1, 0)
            return
        }
        pbit(1, 1)
        pbit(2, animatedBlock.builder.index)
        animatedBlock.builder.buildAnimatorBlock(this, animatedBlock.animatorType)
        if (rendering) {
            viewport.localRenderUpdates.add(index)
        }
    }

    private fun ByteBuffer.updateOtherPlayers(viewport: Viewport) {
        pbit(8, 0)
        for (player in viewport.players) {
            continue // TODO
        }
    }

    private fun ByteBuffer.updateNewPlayers(viewport: Viewport) {
        // TODO
        if (viewport.localRenderUpdates.isNotEmpty()) {
            pbit(11, 2047)
        }
    }

    private fun ByteBuffer.updatePlayerMasks(viewport: Viewport, packet: PlayerInfoPacket) {
        for (index in viewport.localRenderUpdates) {
            val render = packet.highDefinitionRenders[index] ?: continue
            pdata(render)
        }
        viewport.localRenderUpdates.clear()
    }
}
