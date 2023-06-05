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
    override fun handlePacket(packet: RequestMapPacket, player: Player) {
        player.sendRequestedMaps(packet.requestedLands, maxLength = 250)
        player.sendRequestedMaps(packet.requestedLocs, maxLength = 250)
    }

    private fun Player.sendRequestedMaps(requested: Map<String, Pair<Int, Int>>, maxLength: Int) {
        requested.forEach { entry ->
            val map = maps.firstOrNull { it.name == entry.key } ?: return@forEach
            val x = entry.value.first
            val z = entry.value.second
            val bytes = map.bytes
            val length = bytes.size
            val type = map.name[0].toString()

            val slices = (length / maxLength) + 1
            repeat(slices) { slice ->
                val offset = slice * (maxLength - 1)
                val sliceLength = minOf((slice + 1) * maxLength, length)
                val sliceBytes = bytes.sliceArray(offset until sliceLength)
                when (type) {
                    "m" -> client.writePacket(DataLandPacket(x, z, offset, length, sliceBytes))
                    "l" -> client.writePacket(DataLocPacket(x, z, offset, length, sliceBytes))
                }
            }
            when (type) {
                "m" -> client.writePacket(DataLandDonePacket(x, z))
                "l" -> client.writePacket(DataLocDonePacket(x, z))
            }
        }
    }
}
