package kt225.packet.type.server

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
@JvmInline
value class LoadAreaPacket(
    val packed: Int
) : Packet {
    constructor(
        zoneX: Int,
        zoneZ: Int
    ) : this(
        zoneX and 0x3ff
            or (zoneZ and 0x3ff shl 10)
    ) {
        require(this.zoneX == zoneX)
        require(this.zoneZ == zoneZ)
    }
    
    inline val zoneX: Int
        get() = packed and 0x3ff
    
    inline val zoneZ: Int
        get() = packed shr 10 and 0x3ff
}
