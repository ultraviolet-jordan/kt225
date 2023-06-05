package kt225.packet225.reader

import com.google.inject.Singleton
import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.PacketReader
import kt225.common.packet.server.MapRequest
import kt225.packet225.type.client.RequestMapPacket

/**
 * @author Jordan Abraham
 */
@Singleton
class RequestMapPacketReader : PacketReader<RequestMapPacket>(
    id = 150,
    length = -1
) {
    override suspend fun readPacket(buffer: RSByteBuffer, length: Int): RequestMapPacket? {
        val size = buffer.capacity() / 3
        if (size == 0) {
            return null
        }
        val mapRequests = buildList {
            repeat(size) {
                val type = buffer.g1()
                val x = buffer.g1()
                val z = buffer.g1()
                val name = "${if (type == 0) "m" else "l"}${x}_$z"
                add(MapRequest(type, x, z, name))
            }
        }
        return RequestMapPacket(mapRequests)
    }
}
