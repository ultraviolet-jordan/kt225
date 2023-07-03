package kt225.packet.reader

import com.google.inject.Singleton
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.g2
import kt225.common.packet.PacketReader
import kt225.packet.type.client.MoveGamePacket
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class MoveGamePacketReader : PacketReader<MoveGamePacket>(
    id = 181,
    length = -1
) {
    override suspend fun readPacket(buffer: ByteBuffer, length: Int): MoveGamePacket? {
        val ctrlDown = buffer.g1
        val startX = buffer.g2
        val startZ = buffer.g2
        
        // The client does calculate the path, but we will not rely on the client
        // for this information. We are strictly relying on the server for generating
        // the path and keeping the server and client in sync properly.
        // Here we only do math to grab the destination coordinates.
        val checkpoints = buffer.remaining() / 2
        if (checkpoints == 0) {
            return MoveGamePacket(ctrlDown, startX, startZ)
        }
        val path = Array(checkpoints) { IntArray(2) { buffer.g1b } }
        val destinationX = path.last()[0] + startX
        val destinationZ = path.last()[1] + startZ
        return MoveGamePacket(ctrlDown, destinationX, destinationZ)
    }
}
