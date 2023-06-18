package kt225.packet.type.client

import kt225.common.packet.Packet
import kt225.common.packet.server.MapRequest

/**
 * @author Jordan Abraham
 */
data class RequestMapPacket(
    val requests: Array<MapRequest>
) : Packet
