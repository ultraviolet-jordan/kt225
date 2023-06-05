package kt225.game

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.common.packet.Packet
import kt225.common.packet.PacketBuilder
import kt225.common.packet.PacketHandler
import kt225.common.packet.PacketReader
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
@Singleton
data class GamePacketConfiguration @Inject constructor(
    val builders: Map<KClass<*>, PacketBuilder<Packet>>,
    val readers: Set<PacketReader<Packet>>,
    val handlers: Map<KClass<*>, PacketHandler<Packet>>,
)
