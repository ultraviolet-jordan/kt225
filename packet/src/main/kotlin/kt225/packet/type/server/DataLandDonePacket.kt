package kt225.packet.type.server

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class DataLandDonePacket(
    val x: Int,
    val z: Int
) : Packet
