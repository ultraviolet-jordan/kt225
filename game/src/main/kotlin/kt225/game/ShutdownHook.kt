package kt225.game

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.stop
import io.ktor.util.logging.Logger
import kt225.common.game.Server
import kt225.common.game.Synchronizer
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class ShutdownHook(
    private val logger: Logger,
    private val applicationEngine: ApplicationEngine,
    private val server: Server,
    private val synchronizer: Synchronizer
) : Thread() {
    override fun run() {
        logger.info("Running shutdown hook...")
        logger.info("Shutting down the server...")
        server.close()
        logger.info("Shutting down the synchronizer...")
        synchronizer.stop()
        logger.info("Shutting down the application engine...")
        applicationEngine.stop(3, 5, TimeUnit.SECONDS)
    }
}
