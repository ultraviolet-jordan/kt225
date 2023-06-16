package kt225.common.packet

import kt225.common.game.Client

/**
 * @author Jordan Abraham
 */
abstract class PacketHandler<out T : Packet>(
    val groupId: Int
) {
    abstract fun handlePacket(packet: @UnsafeVariance T, client: Client)
}
