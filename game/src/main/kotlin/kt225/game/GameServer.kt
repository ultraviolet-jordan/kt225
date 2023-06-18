package kt225.game

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.network.sockets.ServerSocket
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.application.host
import io.ktor.server.application.port
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.stop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kt225.cache.Cache
import kt225.common.game.Server
import kt225.common.packet.Packet
import kt225.common.packet.PacketReader
import kt225.network.NetworkSession
import kt225.network.NetworkSessionCodecs
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
@Singleton
class GameServer @Inject constructor(
    private val serverSocket: ServerSocket,
    private val applicationEngine: ApplicationEngine,
    private val applicationEnvironment: ApplicationEnvironment,
    private val cache: Cache,
    private val gamePacketConfiguration: GamePacketConfiguration,
    private val networkSessionCodecHandlers: NetworkSessionCodecs
) : Server {
    private val readers = gamePacketConfiguration.readers.associateBy(PacketReader<Packet>::id)
    private val clientPacketLengths = applicationEnvironment.config.property("game.packet.lengths").getList().map(String::toInt).toIntArray()

    override fun bind() = runBlocking {
        val logger = applicationEnvironment.log
        logger.info("Game server is responding at ${applicationEnvironment.config.host}:${applicationEnvironment.config.port}...")
        while (true) {
            val socket = serverSocket.accept()
            val session = NetworkSession(
                socket = socket,
                builders = gamePacketConfiguration.builders,
                readers = readers,
                handlers = gamePacketConfiguration.handlers,
                codecs = networkSessionCodecHandlers,
                crcs = cache.crcs,
                clientPacketLengths = clientPacketLengths
            )
            launch(Dispatchers.IO) {
                logger.info("Connection from ${socket.remoteAddress}")
                session.accept()
            }
        }
    }

    override fun close() {
        serverSocket.close()
        applicationEngine.stop(3, 5, TimeUnit.SECONDS)
    }
}
