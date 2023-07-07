package kt225.packet.type.client

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class MoveGamePacket(
    val ctrlDown: Int,
    val destinationX: Int,
    val destinationZ: Int
) : Packet
