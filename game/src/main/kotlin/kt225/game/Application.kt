package kt225.game

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine
import kt225.cache.CacheModule
import kt225.cache225.Cache225Module
import kt225.network.NetworkModule
import kt225.packet.PacketModule

/**
 * @author Jordan Abraham
 */
fun main(args: Array<String>) {
    val injector = Guice.createInjector(
        CacheModule,
        Cache225Module,
        NetworkModule,
        PacketModule,
        GameModule(args)
    )

    val applicationEnvironment = injector.getInstance<ApplicationEnvironment>()
    val applicationEngine = injector.getInstance<ApplicationEngine>()
    val server = injector.getInstance<GameServer>()
    val synchronizer = injector.getInstance<GameSynchronizer>()
    Runtime.getRuntime().addShutdownHook(ShutdownHook(applicationEnvironment.log, applicationEngine, server, synchronizer))

    try {
        synchronizer.start()
        server.bind()
    } catch (exception: Exception) {
        exception.printStackTrace()
        server.close()
        synchronizer.stop()
    }
}
