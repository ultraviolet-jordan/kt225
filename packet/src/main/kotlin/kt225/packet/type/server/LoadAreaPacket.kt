package kt225.packet.type.server

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class LoadAreaPacket(
    val zoneX: Int,
    val zoneZ: Int
) : Packet
