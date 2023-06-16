package kt225.packet.builder

import com.google.inject.Singleton
import kt225.common.buffer.p1
import kt225.common.packet.PacketBuilder
import kt225.packet.type.server.DataLocDonePacket
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class DataLocDonePacketBuilder : PacketBuilder<DataLocDonePacket>(
    id = 20,
    length = 2
) {
    override fun buildPacket(packet: DataLocDonePacket, buffer: ByteBuffer) {
        buffer.p1(packet.x)
        buffer.p1(packet.z)
    }
}
