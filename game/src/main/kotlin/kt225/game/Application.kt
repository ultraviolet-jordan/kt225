package kt225.game

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine
import kt225.cache.Cache
import kt225.cache.CacheModule
import kt225.cache225.Cache225Module
import kt225.network.NetworkModule
import kt225.packet.PacketModule
import kotlin.system.measureTimeMillis

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

    val server = injector.getInstance<GameServer>()
    val synchronizer = injector.getInstance<GameSynchronizer>()
    
    try {
        val applicationEnvironment = injector.getInstance<ApplicationEnvironment>()
        val time = measureTimeMillis {
            val applicationEngine = injector.getInstance<ApplicationEngine>()
            val cache = injector.getInstance<Cache>()
            Runtime.getRuntime().addShutdownHook(ShutdownHook(applicationEnvironment.log, applicationEngine, server, synchronizer))

            cache.release()
            System.gc()

            synchronizer.start()
        }
        applicationEnvironment.log.info("Game took $time ms to start.")
        server.bind()
    } catch (exception: Exception) {
        exception.printStackTrace()
        server.close()
        synchronizer.stop()
    }
}
