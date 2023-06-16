package kt225.packet.builder

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.map.Maps
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.p4
import kt225.common.packet.PacketBuilder
import kt225.packet.type.server.LoadAreaPacket
import java.nio.ByteBuffer

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
    override fun buildPacket(packet: LoadAreaPacket, buffer: ByteBuffer) {
        val zoneX = packet.zoneX
        val zoneZ = packet.zoneZ

        buffer.p2(zoneX)
        buffer.p2(zoneZ)

        val zonesX = ((zoneX - 6) / 8..(zoneX + 6) / 8)
        val zonesZ = ((zoneZ - 6) / 8..(zoneZ + 6) / 8)

        zonesX.forEach { x ->
            zonesZ.forEach { z ->
                buffer.p1(x)
                buffer.p1(z)
                buffer.p4(maps.firstOrNull { it.name == "m${x}_$z" }?.crc ?: 0)
                buffer.p4(maps.firstOrNull { it.name == "l${x}_$z" }?.crc ?: 0)
            }
        }
    }
}
