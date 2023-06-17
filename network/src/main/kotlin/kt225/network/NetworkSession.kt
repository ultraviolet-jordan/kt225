package kt225.network

import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kt225.common.network.Session
import kt225.common.packet.Packet
import kt225.common.packet.PacketBuilder
import kt225.common.packet.PacketHandler
import kt225.common.packet.PacketReader
import kt225.network.codec.encode.ServerSeedEncoder
import kt225.network.codec.type.ServerSeedResponse
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
class NetworkSession(
    socket: Socket,
    builders: Map<KClass<*>, PacketBuilder<Packet>>,
    readers: Map<Int, PacketReader<Packet>>,
    handlers: Map<KClass<*>, PacketHandler<Packet>>,
    codecs: NetworkSessionCodecs,
    crcs: IntArray
) : Session(
    socket = socket,
    readChannel = socket.openReadChannel(),
    writeChannel = socket.openWriteChannel(),
    builders = builders,
    readers = readers,
    handlers = handlers,
    encoders = codecs.encoders,
    decoders = codecs.decoders,
    crcs = crcs
) {
    override suspend fun accept() {
        try {
            codec(
                type = ServerSeedEncoder::class,
                message = ServerSeedResponse(
                    seed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
                )
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            socket.close()
        }
    }
}
