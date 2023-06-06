package kt225.packet225.builder

import com.google.inject.Singleton
import kt225.common.buffer.BitAccess
import kt225.common.buffer.pArrayBuffer
import kt225.common.buffer.withBitAccess
import kt225.common.game.entity.player.Player
import kt225.common.game.entity.player.Viewport
import kt225.common.packet.PacketBuilder
import kt225.packet225.type.server.PlayerInfoPacket
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
        buffer.withBitAccess {
            updateLocalPlayer(observing, packet, viewport)
            updateOtherPlayers(viewport)
            updateNewPlayers(viewport)
        }
        buffer.updatePlayerMasks(viewport, packet)
    }

    private fun BitAccess.updateLocalPlayer(player: Player, packet: PlayerInfoPacket, viewport: Viewport) {
        val index = player.index
        val rendering = packet.highDefinitionRenders[index] != null
        val activityType = activityType(viewport, player, rendering)
        if (activityType == null) {
            pBit(1, 0)
            return
        }
        pBit(1, 1)
        activityType.pBits(this, packet, viewport, player, rendering)
        if (rendering) {
            viewport.localRenderUpdates.add(index)
        }
    }

    private fun BitAccess.updateOtherPlayers(viewport: Viewport) {
        pBit(8, viewport.players.size)
        for (player in viewport.players) {
            continue // TODO
        }
    }

    private fun BitAccess.updateNewPlayers(viewport: Viewport) {
        // TODO
        if (viewport.localRenderUpdates.isNotEmpty()) {
            pBit(11, 2047)
        }
    }

    private fun ByteBuffer.updatePlayerMasks(viewport: Viewport, packet: PlayerInfoPacket) {
        for (index in viewport.localRenderUpdates) {
            val render = packet.highDefinitionRenders[index] ?: continue
            pArrayBuffer(render)
        }
        viewport.localRenderUpdates.clear()
    }

    private fun activityType(viewport: Viewport, player: Player, rendering: Boolean): ActivityType? = when {
        viewport.players[player.index] != player && rendering -> ActivityType.Placement
        else -> null
    }

    private sealed interface ActivityType {
        fun pBits(bitAccess: BitAccess, packet: PlayerInfoPacket, viewport: Viewport, player: Player, rendering: Boolean)

        object Placement : ActivityType {
            override fun pBits(bitAccess: BitAccess, packet: PlayerInfoPacket, viewport: Viewport, player: Player, rendering: Boolean) {
                val position = player.position
                bitAccess.pBit(2, 3)
                bitAccess.pBit(2, position.plane)
                bitAccess.pBit(7, position.x - position.zoneOriginX)
                bitAccess.pBit(7, position.z - position.zoneOriginZ)
                bitAccess.pBit(1, 1)
                bitAccess.pBit(1, if (rendering) 1 else 0)
                viewport.players[player.index] = player
            }
        }
    }
}
