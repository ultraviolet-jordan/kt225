package kt225.packet225.builder

import com.google.inject.Singleton
import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.PacketBuilder
import kt225.packet225.type.server.PlayerInfoPacket

/**
 * @author Jordan Abraham
 */
@Singleton
class PlayerInfoPacketBuilder : PacketBuilder<PlayerInfoPacket>(
    id = 184,
    length = -2
) {
    override fun buildPacket(packet: PlayerInfoPacket, buffer: RSByteBuffer) {
        buffer.accessBits()
        buffer.writeBits(1, 1)
        buffer.writeBits(2, 3)
        buffer.writeBits(2, 0)
        buffer.writeBits(7, 3222 - 8 * ((3222 shr 3) - 6))
        buffer.writeBits(7, 3222 - 8 * ((3222 shr 3) - 6))
        buffer.writeBits(1, 1)
        buffer.writeBits(1, 0)
        buffer.writeBits(8, 0)
        buffer.accessBytes()
    }

    override fun variableLength(packet: PlayerInfoPacket): Int = Byte.MAX_VALUE.toInt()
}
