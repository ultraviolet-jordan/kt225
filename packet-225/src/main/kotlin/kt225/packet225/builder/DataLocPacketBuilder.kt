package kt225.packet225.builder

import com.google.inject.Singleton
import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.PacketBuilder
import kt225.packet225.type.server.DataLocPacket

/**
 * @author Jordan Abraham
 */
@Singleton
class DataLocPacketBuilder : PacketBuilder<DataLocPacket>(
    id = 220,
    length = -2
) {
    override fun buildPacket(packet: DataLocPacket, buffer: RSByteBuffer) {
        buffer.p1(packet.x)
        buffer.p1(packet.z)
        buffer.p2(packet.offset)
        buffer.p2(packet.length)
        buffer.pArrayBuffer(packet.bytes)
    }

    override fun variableLength(packet: DataLocPacket): Int = 6 + packet.bytes.size
}
