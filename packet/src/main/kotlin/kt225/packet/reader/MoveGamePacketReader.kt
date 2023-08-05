@file:Suppress("DuplicatedCode")

package kt225.packet.reader

import com.google.inject.Singleton
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.g2
import kt225.common.buffer.remaining
import kt225.common.buffer.pad
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
        val ctrlDown = buffer.g1
        val startX = buffer.g2
        val startZ = buffer.g2
        val checkpoints = buffer.remaining shr 1
        if (checkpoints == 0) {
            return MoveGamePacket(ctrlDown, startX, startZ)
        }
        // Just grab the last one we need skip the rest.
        // Not using the client generated path. We make our own.
        buffer.pad(checkpoints - 1 shl 1)
        return MoveGamePacket(ctrlDown, buffer.g1b + startX, buffer.g1b + startZ)
    }
}
