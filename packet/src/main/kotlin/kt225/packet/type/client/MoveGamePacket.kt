package kt225.packet.type.client

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MoveGamePacket(
    val packed: Int
) : Packet {
    constructor(
        ctrlDown: Int,
        destinationX: Int,
        destinationZ: Int
    ) : this (
        ctrlDown and 0x1
            or (destinationX and 0x3fff shl 1)
            or (destinationZ and 0x3fff shl 15)
    ) {
        require(this.ctrlDown == (ctrlDown == 1))
        require(this.destinationX == destinationX)
        require(this.destinationZ == destinationZ)
    }
    
    inline val ctrlDown: Boolean
        get() = packed and 0x1 == 1
    
    inline val destinationX: Int
        get() = packed shr 1 and 0x3fff

    inline val destinationZ: Int
        get() = packed shr 15 and 0x3fff
}
