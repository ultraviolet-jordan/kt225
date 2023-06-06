package kt225.common.packet

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
abstract class PacketBuilder<out T : Packet>(
    val id: Int,
    val length: Int
) {
    abstract fun buildPacket(packet: @UnsafeVariance T, buffer: ByteBuffer)
}
