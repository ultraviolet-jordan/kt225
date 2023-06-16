package kt225.packet.type.server

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class DataLocDonePacket(
    val x: Int,
    val z: Int
) : Packet
