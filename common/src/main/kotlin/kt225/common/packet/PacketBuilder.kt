package kt225.common.packet

import kt225.common.buffer.RSByteBuffer

/**
 * @author Jordan Abraham
 */
abstract class PacketBuilder<out T : Packet>(
    val id: Int,
    val length: Int
) {
    abstract fun buildPacket(packet: @UnsafeVariance T, buffer: RSByteBuffer)
}
