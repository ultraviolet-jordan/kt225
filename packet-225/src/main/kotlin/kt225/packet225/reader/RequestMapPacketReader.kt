package kt225.packet225.reader

import com.google.inject.Singleton
import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.PacketReader
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
        val requestedLands = HashMap<String, Pair<Int, Int>>()
        val requestedLocs = HashMap<String, Pair<Int, Int>>()
        repeat(size) {
            val type = buffer.g1()
            val x = buffer.g1()
            val z = buffer.g1()
            if (type == 0) {
                requestedLands["m${x}_$z"] = x to z
            } else {
                requestedLocs["l${x}_$z"] = x to z
            }
        }
        return RequestMapPacket(requestedLands, requestedLocs)
    }
}
