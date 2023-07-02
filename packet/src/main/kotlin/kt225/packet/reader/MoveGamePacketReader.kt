package kt225.packet.reader

import com.google.inject.Singleton
import kt225.common.buffer.g1
import kt225.common.buffer.g2
import kt225.common.buffer.skip
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
    override suspend fun readPacket(buffer: ByteBuffer, length: Int): MoveGamePacket {
        val ctrlDown = buffer.g1 == 1
        val destinationX = buffer.g2
        val destinationZ = buffer.g2
        
        // The client does calculate the path, but we will not rely on the client
        // for this information. We are strictly relying on the server for generating
        // the path and keeping the server and client in sync properly.
        repeat(buffer.remaining() / 2) {
            buffer.skip(1) // x
            buffer.skip(1) // z
        }
        
        return MoveGamePacket(ctrlDown, destinationX, destinationZ)
    }
}
