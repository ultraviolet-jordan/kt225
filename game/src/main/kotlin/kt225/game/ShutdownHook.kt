package kt225.game

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.stop
import io.ktor.util.logging.Logger
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class ShutdownHook(
    private val logger: Logger,
    private val applicationEngine: ApplicationEngine
) : Thread() {
    override fun run() {
        logger.info("Running shutdown hook...")
        logger.info("Shutting down the application engine...")
        applicationEngine.stop(3, 5, TimeUnit.SECONDS)
    }
}
