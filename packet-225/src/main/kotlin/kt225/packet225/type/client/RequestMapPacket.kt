package kt225.packet225.type.client

import kt225.common.packet.Packet
import kt225.common.packet.server.MapRequest

/**
 * @author Jordan Abraham
 */
data class RequestMapPacket(
    val mapRequests: List<MapRequest>
) : Packet
