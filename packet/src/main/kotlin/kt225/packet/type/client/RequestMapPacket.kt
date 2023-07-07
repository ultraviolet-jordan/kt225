package kt225.packet.type.client

import kt225.common.packet.Packet
import kt225.common.packet.server.MapRequest

/**
 * @author Jordan Abraham
 */
data class RequestMapPacket(
    val requests: Array<MapRequest>
) : Packet {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RequestMapPacket

        return requests.contentEquals(other.requests)
    }

    override fun hashCode(): Int {
        return requests.contentHashCode()
    }
}
