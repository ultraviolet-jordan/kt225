package kt225.common.network

import io.ktor.network.sockets.Socket
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.game.Client
import kt225.common.packet.Packet
import kt225.common.packet.PacketBuilder
import kt225.common.packet.PacketHandler
import kt225.common.packet.PacketReader
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
abstract class Session(
    private val socket: Socket,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel,
    val builders: Map<KClass<*>, PacketBuilder<Packet>>,
    val readers: Map<Int, PacketReader<Packet>>,
    val handlers: Map<KClass<*>, PacketHandler<Packet>>,
    val encoders: Set<CodecEncoder<CodecEncoderType>>,
    val decoders: Set<CodecDecoder>,
    val crcs: IntArray
) {
    var client: Client? = null

    abstract suspend fun accept()

    fun attach(client: Client) {
        this.client = client
    }

    suspend inline fun <reified C : CodecEncoderType, reified T : CodecEncoder<C>> codec(type: KClass<T>, message: CodecEncoderType) {
        encoders.find(type.java::isInstance)?.encode(this, message, writeChannel)
    }

    suspend inline fun <reified T : CodecDecoder> codec(type: KClass<T>) {
        decoders.find(type.java::isInstance)?.decode(this, readChannel)
    }

    fun close() {
        socket.close()
    }
}
