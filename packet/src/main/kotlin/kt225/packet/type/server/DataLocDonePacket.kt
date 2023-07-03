package kt225.packet.type.server

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
@JvmInline
value class DataLocDonePacket(
    val packed: Int
) : Packet {
    constructor(
        x: Int,
        z: Int
    ) : this (
        x and 0xff
            or (z and 0xff shl 8)
    ) {
        require(this.x == x)
        require(this.z == z)
    }

    inline val x: Int
        get() = packed and 0xff

    inline val z: Int
        get() = packed shr 8 and 0xff
}
