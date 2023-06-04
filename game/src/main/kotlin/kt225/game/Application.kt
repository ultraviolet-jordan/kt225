package kt225.game

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine
import kt225.cache.CacheModule

/**
 * @author Jordan Abraham
 */
fun main(args: Array<String>) {
    val injector = Guice.createInjector(
        CacheModule(),
        GameModule(args)
    )
    val applicationEnvironment = injector.getInstance<ApplicationEnvironment>()
    val applicationEngine = injector.getInstance<ApplicationEngine>()
    val server = injector.getInstance<GameServer>()
    try {
        Runtime.getRuntime().addShutdownHook(ShutdownHook(applicationEnvironment.log, applicationEngine))
        server.bind()
    } catch (exception: Exception) {
        exception.printStackTrace()
        server.close()
    }
}
