package kt225.packet225.builder

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.maps.Maps
import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.PacketBuilder
import kt225.packet225.type.server.LoadAreaPacket

/**
 * @author Jordan Abraham
 */
@Singleton
class LoadAreaPacketBuilder @Inject constructor(
    private val maps: Maps
) : PacketBuilder<LoadAreaPacket>(
    id = 237,
    length = -2
) {
    override fun buildPacket(packet: LoadAreaPacket, buffer: RSByteBuffer) {
        val zoneX = packet.zoneX
        val zoneZ = packet.zoneZ

        buffer.p2(zoneX)
        buffer.p2(zoneZ)

        val zonesX = ((zoneX - 6) / 8..(zoneX + 6) / 8)
        val zonesZ = ((zoneZ - 6) / 8..(zoneZ + 6) / 8)

        for (x in zonesX) {
            for (z in zonesZ) {
                buffer.p1(x)
                buffer.p1(z)
                buffer.p4(maps.firstOrNull { it.name == "m${x}_$z" }?.crc ?: 0)
                buffer.p4(maps.firstOrNull { it.name == "l${x}_$z" }?.crc ?: 0)
            }
        }
    }
}
