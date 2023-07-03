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
        val remaining = buffer.remaining()
        if (remaining != 0) {
            buffer.skip(remaining - 14 - 2)
        }
        val destinationX = if (buffer.remaining() != 0) buffer.g1b + startX else startX
        val destinationZ = if (buffer.remaining() != 0) buffer.g1b + startZ else startZ
        
        // Anti-cheat MiniMap Input
        buffer.skip(1) // clickX
        buffer.skip(1) // clickX
        buffer.skip(2) // cameraYaw
        if (buffer.g1b != 57) { // 57
            return null
        }
        buffer.skip(1) // antiCheatAngle
        buffer.skip(1) // zoom
        if (buffer.g1b != 89) { // 89
            return null
        }
        buffer.skip(2) // currentX
        buffer.skip(2) // currentZ
        buffer.skip(1) // clickedMiniMap
        if (buffer.g1b != 63) { // 63
            return null
        }
        return MoveMiniMapPacket(ctrlDown, destinationX, destinationZ)
    }
}
