package kt225.packet225.builder

import com.google.inject.Singleton
import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.PacketBuilder
import kt225.packet225.type.server.DataLandPacket

/**
 * @author Jordan Abraham
 */
@Singleton
class DataLandPacketBuilder : PacketBuilder<DataLandPacket>(
    id = 132,
    length = -2
) {
    override fun buildPacket(packet: DataLandPacket, buffer: RSByteBuffer) {
        buffer.p1(packet.x)
        buffer.p1(packet.z)
        buffer.p2(packet.offset)
        buffer.p2(packet.length)
        buffer.pArrayBuffer(packet.bytes)
    }
}
