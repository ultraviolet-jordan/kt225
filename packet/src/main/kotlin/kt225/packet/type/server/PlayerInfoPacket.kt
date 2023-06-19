package kt225.packet.type.server

import kt225.common.game.entity.player.Player
import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class PlayerInfoPacket(
    val observing: Player
) : Packet
