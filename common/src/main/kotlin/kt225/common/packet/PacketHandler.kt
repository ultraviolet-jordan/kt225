package kt225.common.packet

import kt225.common.game.entity.player.Player

/**
 * @author Jordan Abraham
 */
abstract class PacketHandler<T : Packet>(
    val groupId: Int
) {
    abstract fun handlePacket(packet: T, player: Player)
}
