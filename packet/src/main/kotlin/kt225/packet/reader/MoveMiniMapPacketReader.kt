@file:Suppress("DuplicatedCode")

package kt225.packet.reader

import com.google.inject.Singleton
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.g2
import kt225.common.buffer.skip
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
    override suspend fun readPacket(buffer: ByteBuffer, length: Int): MoveMiniMapPacket? {
        val ctrlDown = buffer.g1
        val startX = buffer.g2
        val startZ = buffer.g2

        // The client does calculate the path, but we will not rely on the client
        // for this information. We are strictly relying on the server for generating
        // the path and keeping the server and client in sync properly.
        // Here we only do math to grab the destination coordinates.
        val checkpoints = (buffer.remaining() - 14) shr 1
        if (checkpoints == 0) {
            if (!buffer.readAntiCheatInput()) {
                return null
            }
            return MoveMiniMapPacket(ctrlDown, startX, startZ)
        }
        buffer.skip(checkpoints - 1 shl 1)
        val destinationX = buffer.g1b + startX
        val destinationZ = buffer.g1b + startZ
        if (!buffer.readAntiCheatInput()) {
            return null
        }
        return MoveMiniMapPacket(ctrlDown, destinationX, destinationZ)
    }
    
    private fun ByteBuffer.readAntiCheatInput(): Boolean {
        skip(1) // clickX
        skip(1) // clickX
        skip(2) // cameraYaw
        if (g1b != 57) { // 57
            return false
        }
        skip(1) // antiCheatAngle
        skip(1) // zoom
        if (g1b != 89) { // 89
            return false
        }
        skip(2) // currentX
        skip(2) // currentZ
        skip(1) // clickedMiniMap
        return g1b == 63 // 63
    }
}
