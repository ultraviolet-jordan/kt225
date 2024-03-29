@file:Suppress("DuplicatedCode")

package kt225.packet.reader

import com.google.inject.Singleton
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.g2
import kt225.common.buffer.remaining
import kt225.common.buffer.pad
import kt225.common.packet.PacketReader
import kt225.packet.type.client.MoveMiniMapPacket
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class MoveMiniMapPacketReader : PacketReader<MoveMiniMapPacket>(
    id = 165,
    length = -1
) {
    override suspend fun readPacket(buffer: ByteBuffer, length: Int): MoveMiniMapPacket {
        val ctrlDown = buffer.g1
        val startX = buffer.g2
        val startZ = buffer.g2
        val checkpoints = (buffer.remaining - 14) shr 1
        if (checkpoints == 0) {
            buffer.readAntiCheatInput()
            return MoveMiniMapPacket(ctrlDown, startX, startZ)
        }
        // Just grab the last one we need skip the rest.
        // Not using the client generated path. We make our own.
        buffer.pad(checkpoints - 1 shl 1)
        val destinationX = buffer.g1b + startX
        val destinationZ = buffer.g1b + startZ
        buffer.readAntiCheatInput()
        return MoveMiniMapPacket(ctrlDown, destinationX, destinationZ)
    }

    private fun ByteBuffer.readAntiCheatInput() {
        pad(1) // clickX
        pad(1) // clickX
        pad(2) // cameraYaw
        pad(1) // 57
        pad(1) // antiCheatAngle
        pad(1) // zoom
        pad(1) // 89
        pad(2) // currentX
        pad(2) // currentZ
        pad(1) // clickedMiniMap
        pad(1) // 63
    }
}
