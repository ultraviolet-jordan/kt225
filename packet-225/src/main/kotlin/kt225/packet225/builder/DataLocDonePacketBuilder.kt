package kt225.packet225.builder

import com.google.inject.Singleton
import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.PacketBuilder
import kt225.packet225.type.server.DataLocDonePacket

/**
 * @author Jordan Abraham
 */
@Singleton
class DataLocDonePacketBuilder : PacketBuilder<DataLocDonePacket>(
    id = 20,
    length = 2
) {
    override fun buildPacket(packet: DataLocDonePacket, buffer: RSByteBuffer) {
        buffer.p1(packet.x)
        buffer.p1(packet.z)
    }
}
