package kt225.game

import dev.misfitlabs.kotlinguice4.KotlinModule
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.ServerSocket
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine
import kt225.common.game.SynchronizerEntityRenderer
import kt225.common.game.entity.player.Player
import kt225.common.game.world.World
import kt225.game.world.map.CollisionManager
import kt225.game.world.map.CollisionManagerProvider
import kt225.game.world.map.PathFinderProvider
import kt225.game.world.map.StepValidatorProvider
import kt225.game.world.map.ZoneFlagsProvider
import org.rsmod.pathfinder.PathFinder
import org.rsmod.pathfinder.StepValidator
import org.rsmod.pathfinder.ZoneFlags
import java.security.interfaces.RSAPrivateCrtKey
import java.util.concurrent.ExecutorService
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
        bind<ZoneFlags>().toProvider<ZoneFlagsProvider>().asEagerSingleton()
        bind<PathFinder>().toProvider<PathFinderProvider>().asEagerSingleton()
        bind<StepValidator>().toProvider<StepValidatorProvider>().asEagerSingleton()
        bind<CollisionManager>().toProvider<CollisionManagerProvider>().asEagerSingleton()
        bind<World>().toProvider<GameWorldProvider>().asEagerSingleton()
        bind<ScheduledExecutorService>().toProvider<GameThreadExecutorProvider>().asEagerSingleton()
        bind<ExecutorService>().toProvider<EntityPoolExecutorProvider>().asEagerSingleton()
        bind<ServerSocket>().toProvider<ServerSocketProvider>().asEagerSingleton()
        bind<SynchronizerEntityRenderer<Player>>().to<PlayerSynchronizerRenderer>().asEagerSingleton()
        bind<RSAPrivateCrtKey>().toProvider<RSAPrivateCrtKeyProvider>()
    }
}
