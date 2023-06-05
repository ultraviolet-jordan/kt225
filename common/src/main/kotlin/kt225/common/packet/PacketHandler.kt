package kt225.common.packet

import kt225.common.game.entity.player.Player

/**
 * @author Jordan Abraham
 */
abstract class PacketHandler<out T : Packet>(
    val groupId: Int
) {
    abstract fun handlePacket(packet: @UnsafeVariance T, player: Player)
}
