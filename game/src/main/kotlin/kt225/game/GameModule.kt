package kt225.game

import dev.misfitlabs.kotlinguice4.KotlinModule
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine
import kt225.common.game.world.World
import java.util.concurrent.ScheduledExecutorService

/**
 * @author Jordan Abraham
 */
class GameModule(
    private val args: Array<String>
) : KotlinModule() {
    override fun configure() {
        bind<ApplicationArguments>().toInstance(ApplicationArguments(args))
        bind<ApplicationEnvironment>().toProvider<ApplicationEnvironmentProvider>().asEagerSingleton()
        bind<ApplicationEngine>().toProvider<ApplicationEngineProvider>().asEagerSingleton()
        bind<SelectorManager>().toProvider<ServerSocketSelectorProvider>().asEagerSingleton()
        bind<World>().toProvider<GameWorldProvider>().asEagerSingleton()
        bind<ScheduledExecutorService>().toProvider<GameThreadExecutorProvider>().asEagerSingleton()
        bind<ServerSocket>().toProvider<ServerSocketProvider>().asEagerSingleton()
    }
}
