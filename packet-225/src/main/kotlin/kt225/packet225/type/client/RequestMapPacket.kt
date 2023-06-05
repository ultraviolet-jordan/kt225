package kt225.packet225.type.client

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class RequestMapPacket(
    val requestedLands: Map<String, Pair<Int, Int>>,
    val requestedLocs: Map<String, Pair<Int, Int>>
) : Packet
