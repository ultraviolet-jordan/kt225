package kt225.packet.type.server

import kt225.common.game.entity.player.Viewport
import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class PlayerInfoPacket(
    val index: Int,
    val viewport: Viewport
) : Packet
