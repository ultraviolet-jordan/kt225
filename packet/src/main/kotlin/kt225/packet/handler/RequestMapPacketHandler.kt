package kt225.packet.handler

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.map.MapLands
import kt225.cache.map.MapLocs
import kt225.common.game.Client
import kt225.common.packet.PacketHandler
import kt225.packet.type.client.RequestMapPacket
import kt225.packet.type.server.DataLandDonePacket
import kt225.packet.type.server.DataLandPacket
import kt225.packet.type.server.DataLocDonePacket
import kt225.packet.type.server.DataLocPacket

/**
 * @author Jordan Abraham
 */
@Singleton
class RequestMapPacketHandler @Inject constructor(
    private val mapLands: MapLands,
    private val mapLocs: MapLocs
) : PacketHandler<RequestMapPacket>(
    groupId = 0
) {
    override fun handlePacket(packet: RequestMapPacket, client: Client) {
        packet.requests.forEach { request ->
            val (type, x, z) = request
            val map = when (type) {
                0 -> mapLands.firstOrNull { it.name == "m${x}_$z" }
                1 -> mapLocs.firstOrNull { it.name == "l${x}_$z" }
                else -> null
            } ?: return@forEach
            val bytes = map.bytes
            val zipped = bytes.size

            val donePacket = when (type) {
                0 -> DataLandDonePacket(x, z)
                1 -> DataLocDonePacket(x, z)
                else -> null
            }

            // We must limit the raw length of bytes to send at a time.
            // The 225 client has a 5000 byte buffer array limit.
            // Any packet sent with > 5000 bytes (excluding the packet header etc), will crash the client.
            // We slice the maps up as many as we want and send each slice of bytes to the client individually.
            val sliceLimit = 4994 // 5000 - 6 for packet header/length room.
            val slices = (zipped / sliceLimit) + 1
            repeat(slices) { slice ->
                val offset = slice * (sliceLimit - 1)
                val length = minOf((slice + 1) * sliceLimit, zipped)
                val sliced = bytes.copyOfRange(offset, length)
                val packetToSend = when (type) {
                    0 -> DataLandPacket(x, z, offset, zipped, sliced)
                    1 -> DataLocPacket(x, z, offset, zipped, sliced)
                    else -> null
                }
                packetToSend?.let {
                    client.writePacketDirect(it, sliced.size + 6)
                }
            }

            donePacket?.let {
                client.writePacketDirect(it, 2)
            }
        }
    }
}
