package kt225.game

import dev.misfitlabs.kotlinguice4.KotlinModule
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine

/**
 * @author Jordan Abraham
 */
class GameModule(
    private val args: Array<String>
) : KotlinModule() {
    override fun configure() {
        bind<ApplicationArguments>().toInstance(ApplicationArguments(args))
        bind<ApplicationEnvironment>().toProvider<ApplicationEnvironmentProvider>()
        bind<ApplicationEngine>().toProvider<ApplicationEngineProvider>()
        bind<SelectorManager>().toProvider<ServerSocketSelectorProvider>()
        bind<ServerSocket>().toProvider<ServerSocketProvider>()
    }
}
