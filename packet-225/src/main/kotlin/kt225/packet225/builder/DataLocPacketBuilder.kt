package kt225.packet225.builder

import com.google.inject.Singleton
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.pArrayBuffer
import kt225.common.packet.PacketBuilder
import kt225.packet225.type.server.DataLocPacket
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class DataLocPacketBuilder : PacketBuilder<DataLocPacket>(
    id = 220,
    length = -2
) {
    override fun buildPacket(packet: DataLocPacket, buffer: ByteBuffer) {
        buffer.p1(packet.x)
        buffer.p1(packet.z)
        buffer.p2(packet.offset)
        buffer.p2(packet.length)
        buffer.pArrayBuffer(packet.bytes)
    }
}
