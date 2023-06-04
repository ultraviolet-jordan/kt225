package kt225.common.packet

import kt225.common.buffer.RSByteBuffer

/**
 * @author Jordan Abraham
 */
abstract class PacketBuilder<T : Packet>(
    val id: Int,
    val length: Int
) {
    abstract fun buildPacket(packet: T, buffer: RSByteBuffer)
}
