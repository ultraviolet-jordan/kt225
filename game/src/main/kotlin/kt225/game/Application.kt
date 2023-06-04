package kt225.game

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import io.ktor.network.sockets.ServerSocket
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.application.host
import io.ktor.server.application.port
import io.ktor.server.engine.ApplicationEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author Jordan Abraham
 */
fun main(args: Array<String>) {
    try {
        val injector = Guice.createInjector(GameModule(args))
        val applicationEnvironment = injector.getInstance<ApplicationEnvironment>()
        val applicationEngine = injector.getInstance<ApplicationEngine>()
        val serverSocket = injector.getInstance<ServerSocket>()

        Runtime.getRuntime().addShutdownHook(ShutdownHook(applicationEnvironment.log, applicationEngine))

        runBlocking {
            val logger = applicationEnvironment.log
            logger.info("Game application is responding at ${applicationEnvironment.config.host}:${applicationEnvironment.config.port}...")
            while (true) {
                val socket = serverSocket.accept()
                val client = GameClient(socket)
                launch(Dispatchers.IO) { client.accept() }
            }
        }
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}
