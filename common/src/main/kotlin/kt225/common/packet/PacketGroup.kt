package kt225.common.packet

/**
 * @author Jordan Abraham
 */
class PacketGroup(
    val packet: Packet,
    val handler: PacketHandler<Packet>
)
