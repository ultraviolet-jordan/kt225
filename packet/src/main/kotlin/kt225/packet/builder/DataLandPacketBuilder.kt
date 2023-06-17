package kt225.packet.builder

import com.google.inject.Singleton
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.pArrayBuffer
import kt225.common.packet.PacketBuilder
import kt225.packet.type.server.DataLandPacket
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class DataLandPacketBuilder : PacketBuilder<DataLandPacket>(
    id = 132,
    length = -2
) {
    override fun buildPacket(packet: DataLandPacket, buffer: ByteBuffer) {
        val (x, z, offset, length, bytes) = packet
        buffer.p1(x)
        buffer.p1(z)
        buffer.p2(offset)
        buffer.p2(length)
        buffer.pArrayBuffer(bytes)
    }
}
