package kt225.packet.builder

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.common.buffer.accessBits
import kt225.common.buffer.pbit
import kt225.common.buffer.pdata
import kt225.common.game.SynchronizerEntityRenderer
import kt225.common.game.entity.player.Player
import kt225.common.game.entity.player.Viewport
import kt225.common.packet.PacketBuilder
import kt225.packet.type.server.PlayerInfoPacket
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class PlayerInfoPacketBuilder @Inject constructor(
    private val synchronizerEntityRenderer: SynchronizerEntityRenderer<Player>
) : PacketBuilder<PlayerInfoPacket>(
    id = 184,
    length = -2
) {
    private val highDefinitionRenders = synchronizerEntityRenderer.highDefinitionRenders
    private val animatorRenders = synchronizerEntityRenderer.animatorRenders
    
    override fun buildPacket(packet: PlayerInfoPacket, buffer: ByteBuffer) {
        val viewport = packet.viewport
        buffer.accessBits {
            updateLocalPlayer(packet.index, viewport)
            updateOtherPlayers(viewport)
            updateNewPlayers(viewport)
        }
        buffer.updatePlayerMasks(viewport, packet)
    }

    private fun ByteBuffer.updateLocalPlayer(index: Int, viewport: Viewport) {
        val rendering = highDefinitionRenders[index] != null
        val animatedBlock = animatorRenders[index]
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
            val render = highDefinitionRenders[index] ?: continue
            pdata(render)
        }
        viewport.localRenderUpdates.clear()
    }
}
