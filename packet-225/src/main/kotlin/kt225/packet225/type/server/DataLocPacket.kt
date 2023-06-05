package kt225.packet225.type.server

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class DataLocPacket(
    val x: Int,
    val z: Int,
    val offset: Int,
    val length: Int,
    val bytes: ByteArray
) : Packet {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataLocPacket

        if (x != other.x) return false
        if (z != other.z) return false
        if (offset != other.offset) return false
        if (length != other.length) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + z
        result = 31 * result + offset
        result = 31 * result + length
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
