package kt225.packet225.builder

import com.google.inject.Singleton
import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.PacketBuilder
import kt225.packet225.type.server.DataLandDonePacket

/**
 * @author Jordan Abraham
 */
@Singleton
class DataLandDonePacketBuilder : PacketBuilder<DataLandDonePacket>(
    id = 80,
    length = 2
) {
    override fun buildPacket(packet: DataLandDonePacket, buffer: RSByteBuffer) {
        buffer.p1(packet.x)
        buffer.p1(packet.z)
    }
}
