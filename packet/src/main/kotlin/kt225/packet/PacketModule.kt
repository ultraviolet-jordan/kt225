package kt225.packet

import dev.misfitlabs.kotlinguice4.KotlinModule
import dev.misfitlabs.kotlinguice4.multibindings.KotlinMapBinder
import dev.misfitlabs.kotlinguice4.multibindings.KotlinMultibinder
import kt225.common.packet.Packet
import kt225.common.packet.PacketBuilder
import kt225.common.packet.PacketHandler
import kt225.common.packet.PacketReader
import kt225.packet.builder.DataLandDonePacketBuilder
import kt225.packet.builder.DataLandPacketBuilder
import kt225.packet.builder.DataLocDonePacketBuilder
import kt225.packet.builder.DataLocPacketBuilder
import kt225.packet.builder.LoadAreaPacketBuilder
import kt225.packet.builder.PlayerInfoPacketBuilder
import kt225.packet.handler.MoveGamePacketHandler
import kt225.packet.handler.RequestMapPacketHandler
import kt225.packet.reader.MoveGamePacketReader
import kt225.packet.reader.RequestMapPacketReader
import kt225.packet.type.client.MoveGamePacket
import kt225.packet.type.client.RequestMapPacket
import kt225.packet.type.server.DataLandDonePacket
import kt225.packet.type.server.DataLandPacket
import kt225.packet.type.server.DataLocDonePacket
import kt225.packet.type.server.DataLocPacket
import kt225.packet.type.server.LoadAreaPacket
import kt225.packet.type.server.PlayerInfoPacket
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object PacketModule : KotlinModule() {
    override fun configure() {
        bindPacketBuilders()
        bindPacketReaders()
        bindPacketHandlers()
    }

    private fun bindPacketBuilders() {
        KotlinMapBinder.newMapBinder<KClass<*>, PacketBuilder<Packet>>(kotlinBinder).apply {
            bindPacketBuilder<LoadAreaPacket, LoadAreaPacketBuilder>()
            bindPacketBuilder<DataLandPacket, DataLandPacketBuilder>()
            bindPacketBuilder<DataLocPacket, DataLocPacketBuilder>()
            bindPacketBuilder<DataLocDonePacket, DataLocDonePacketBuilder>()
            bindPacketBuilder<DataLandDonePacket, DataLandDonePacketBuilder>()
            bindPacketBuilder<PlayerInfoPacket, PlayerInfoPacketBuilder>()
        }
    }

    private fun bindPacketReaders() {
        KotlinMultibinder.newSetBinder<PacketReader<Packet>>(kotlinBinder).apply {
            bindPacketReader<RequestMapPacketReader>()
            bindPacketReader<MoveGamePacketReader>()
        }
    }

    private fun bindPacketHandlers() {
        KotlinMapBinder.newMapBinder<KClass<*>, PacketHandler<Packet>>(kotlinBinder).apply {
            bindPacketHandler<RequestMapPacket, RequestMapPacketHandler>()
            bindPacketHandler<MoveGamePacket, MoveGamePacketHandler>()
        }
    }

    private inline fun <reified T : Packet, reified X : PacketBuilder<T>> KotlinMapBinder<KClass<*>, PacketBuilder<Packet>>.bindPacketBuilder() {
        addBinding(T::class).to<X>()
    }

    private inline fun <reified X : PacketReader<Packet>> KotlinMultibinder<PacketReader<Packet>>.bindPacketReader() {
        addBinding().to<X>()
    }

    private inline fun <reified T : Packet, reified X : PacketHandler<T>> KotlinMapBinder<KClass<*>, PacketHandler<Packet>>.bindPacketHandler() {
        addBinding(T::class).to<X>()
    }
}
