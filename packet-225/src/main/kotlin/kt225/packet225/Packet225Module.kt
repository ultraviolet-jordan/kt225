package kt225.packet225

import dev.misfitlabs.kotlinguice4.KotlinModule
import dev.misfitlabs.kotlinguice4.multibindings.KotlinMapBinder
import dev.misfitlabs.kotlinguice4.multibindings.KotlinMultibinder
import kt225.common.packet.Packet
import kt225.common.packet.PacketBuilder
import kt225.common.packet.PacketHandler
import kt225.common.packet.PacketReader
import kt225.packet225.builder.DataLandDonePacketBuilder
import kt225.packet225.builder.DataLandPacketBuilder
import kt225.packet225.builder.DataLocDonePacketBuilder
import kt225.packet225.builder.DataLocPacketBuilder
import kt225.packet225.builder.LoadAreaPacketBuilder
import kt225.packet225.builder.PlayerInfoPacketBuilder
import kt225.packet225.handler.RequestMapPacketHandler
import kt225.packet225.reader.RequestMapPacketReader
import kt225.packet225.type.client.RequestMapPacket
import kt225.packet225.type.server.DataLandDonePacket
import kt225.packet225.type.server.DataLandPacket
import kt225.packet225.type.server.DataLocDonePacket
import kt225.packet225.type.server.DataLocPacket
import kt225.packet225.type.server.LoadAreaPacket
import kt225.packet225.type.server.PlayerInfoPacket
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
class Packet225Module : KotlinModule() {
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
        }
    }

    private fun bindPacketHandlers() {
        KotlinMapBinder.newMapBinder<KClass<*>, PacketHandler<Packet>>(kotlinBinder).apply {
            bindPacketHandler<RequestMapPacket, RequestMapPacketHandler>()
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
