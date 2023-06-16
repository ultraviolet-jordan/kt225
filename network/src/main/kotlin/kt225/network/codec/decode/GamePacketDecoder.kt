package kt225.network.codec.decode

import com.google.inject.Inject
import com.google.inject.Singleton
import com.runetopic.cryptography.isaac.ISAAC
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.time.withTimeout
import kt225.common.network.CodecDecoder
import kt225.common.network.Session
import kt225.common.packet.Packet
import java.nio.ByteBuffer
import java.time.Duration

/**
 * @author Jordan Abraham
 */
@Singleton
class GamePacketDecoder @Inject constructor(
    applicationEnvironment: ApplicationEnvironment
) : CodecDecoder {
    private val logger = applicationEnvironment.log

    override suspend fun decode(session: Session, channel: ByteReadChannel) {
        while (true) {
            val client = session.client ?: break
            val packet = withTimeout(Duration.ofSeconds(30)) {
                channel.awaitPacket(session, client.clientIsaac)
            } ?: continue
            val handler = session.handlers[packet::class] ?: continue
            handler.handlePacket(packet, client)
        }
    }

    private suspend fun ByteReadChannel.awaitPacket(session: Session, isaac: ISAAC): Packet? {
        val id = ((readByte().toInt() and 0xFF) - isaac.getNext() and 0xFF)
        if (id > PacketLengths.lengths.size) {
            discard(availableForRead.toLong())
            return null
        }
        val serverLength = PacketLengths.lengths[id]
        val clientLength = when {
            serverLength != -1 && serverLength != -2 -> serverLength
            serverLength == -1 -> (readByte().toInt() and 0xFF)
            else -> (readShort().toInt() and 0xFFFF)
        }

        val reader = session.readers[id]
        if (reader == null) {
            logger.info("Reader not found for packet with id: $id")
            discard(clientLength.toLong())
            return null
        }

        if (reader.length != serverLength) {
            logger.debug("Packet reader length and server length mismatch. Packet Reader length is ${reader.length} and server length is $serverLength.")
            discard(clientLength.toLong())
            return null
        }

        if (reader.length != -1 && reader.length != clientLength) {
            logger.debug("Packet length mismatch. Packet Reader length is ${reader.length} and client length was $clientLength.")
            // Discard the bytes from the read channel.
            discard(clientLength.toLong())
            return null
        }

        val buffer = ByteBuffer.allocate(clientLength)
        val readBytes = readFully(buffer)
        if (readBytes != clientLength) {
            logger.info("Packet buffer read bytes mismatch. Read bytes was $readBytes and payload length was $clientLength")
            return null
        }
        val packet = reader.readPacket(buffer.flip(), clientLength) ?: return null

        logger.info("Incoming Packet: Id=$id, ServerLength=$serverLength, ClientLength=$clientLength")
        return packet
    }

    private object PacketLengths {
        val lengths = intArrayOf(
            0, 0, 2, 0, -1, 0, 6, 4, 2, 2, // 0
            0, 8, 0, 0, 0, 0, 0, 1, 0, 0, // 10
            0, 0, 0, 0, 0, 0, 0, 2, 0, 0, // 20
            3, 6, 0, 0, 0, 0, 0, 0, 6, 0, // 30
            6, 0, 0, 0, 0, 0, 0, 0, 8, 0, // 40
            0, 0, 13, 2, 0, 0, 0, 0, 0, 6, // 50
            0, 0, 0, 0, 0, 0, 4, 0, 0, 0, // 60
            0, 6, 0, 0, 0, 6, 0, 0, 0, 8, // 70
            0, -1, 0, 0, 0, 0, 0, 0, 4, 0, // 80
            0, 0, 0, -1, 0, 0, 6, 6, 0, 0, // 90
            2, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 100
            0, 0, 0, 2, 0, 0, 6, 0, 8, 0, // 110
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 120
            12, 0, 0, 6, 4, 0, 0, 0, 8, 0, // 130
            6, 0, 0, 0, 0, 0, -1, 0, 9, 0, // 140
            -1, 0, 0, 0, 0, 2, 0, 6, 3, 6, // 150
            0, 0, 0, 0, 2, -1, 0, 0, 0, 0, // 160
            0, 8, 6, 0, 0, 1, 2, 4, 6, 0, // 170
            0, -1, 0, 0, 0, 2, 0, 0, 0, 6, // 180
            10, 0, 0, 0, 2, 6, 0, 0, 0, 0, // 190
            6, 0, 8, 0, 0, 0, 2, 0, 0, 0, // 200
            0, 6, 6, 0, 0, 3, 0, 0, 0, -1, // 210
            6, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 220
            0, 0, 0, 1, 0, 4, 4, 4, 1, 12, // 230
            0, 0, 0, 0, 3, 6, 0, 6, 8, 0, // 240
            0, 0, 0, 0, 0, 0 // 250
        )
    }
}
