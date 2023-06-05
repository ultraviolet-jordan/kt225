package kt225.common.packet

import kt225.common.buffer.RSByteBuffer

/**
 * @author Jordan Abraham
 */
abstract class PacketReader<out T : Packet>(
    val id: Int,
    val length: Int
) {
    abstract suspend fun readPacket(buffer: RSByteBuffer, length: Int): T?
}
