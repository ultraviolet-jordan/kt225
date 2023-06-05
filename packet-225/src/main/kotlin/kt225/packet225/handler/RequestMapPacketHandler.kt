package kt225.packet225.handler

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.maps.Maps
import kt225.common.game.entity.player.Player
import kt225.common.packet.PacketHandler
import kt225.packet225.type.client.RequestMapPacket
import kt225.packet225.type.server.DataLandDonePacket
import kt225.packet225.type.server.DataLandPacket
import kt225.packet225.type.server.DataLocDonePacket
import kt225.packet225.type.server.DataLocPacket

/**
 * @author Jordan Abraham
 */
@Singleton
class RequestMapPacketHandler @Inject constructor(
    private val maps: Maps
) : PacketHandler<RequestMapPacket>(
    groupId = 0
) {
    private val bytesLengthLimit = 250

    override fun handlePacket(packet: RequestMapPacket, player: Player) {
        val client = player.client

        packet.mapRequests.forEach { request ->
            val map = maps.firstOrNull { it.name == request.name } ?: return@forEach
            val x = request.x
            val z = request.z
            val bytes = map.bytes
            val length = bytes.size
            val type = request.type

            val slices = (length / bytesLengthLimit) + 1
            repeat(slices) { slice ->
                val offset = slice * (bytesLengthLimit - 1)
                val sliceLength = minOf((slice + 1) * bytesLengthLimit, length)
                val sliceBytes = bytes.sliceArray(offset until sliceLength)
                when (type) {
                    0 -> client.writePacket(DataLandPacket(x, z, offset, length, sliceBytes))
                    1 -> client.writePacket(DataLocPacket(x, z, offset, length, sliceBytes))
                }
            }
            when (type) {
                0 -> client.writePacket(DataLandDonePacket(x, z))
                1 -> client.writePacket(DataLocDonePacket(x, z))
            }
        }
    }
}
