package kt225.network.codec.decode

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.time.withTimeout
import kt225.common.crypto.IsaacRandom
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
            client.readPacket(packet)
        }
    }

    private suspend fun ByteReadChannel.awaitPacket(session: Session, isaac: IsaacRandom): Packet? {
        val id = ((readByte().toInt() - isaac.nextInt) and 0xff)
        if (id > session.clientPacketLengths.size) {
            discard(availableForRead.toLong())
            return null
        }
        val serverLength = session.clientPacketLengths[id]
        if (serverLength == -3) {
            discard(availableForRead.toLong())
            return null
        }
        val clientLength = when {
            serverLength != -1 && serverLength != -2 -> serverLength
            serverLength == -1 -> (readByte().toInt() and 0xff)
            else -> (readShort().toInt() and 0xffff)
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
}
