package kt225.common.packet

import io.ktor.utils.io.ByteReadChannel

/**
 * @author Jordan Abraham
 */
abstract class PacketReader<T : Packet>(
    val id: Int,
    val length: Int
) {
    abstract suspend fun readPacket(readChannel: ByteReadChannel, length: Int): T?
}
