package kt225.packet225.type.server

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class LoadAreaPacket(
    val zoneX: Int,
    val zoneZ: Int
) : Packet
