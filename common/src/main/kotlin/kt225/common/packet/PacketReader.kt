package kt225.common.packet

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
abstract class PacketReader<out T : Packet>(
    val id: Int,
    val length: Int
) {
    abstract suspend fun readPacket(buffer: ByteBuffer, length: Int): T?
}
