package kt225.http

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.findBindingsByType
import dev.misfitlabs.kotlinguice4.getInstance
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine
import kt225.cache.Cache
import kt225.cache.CacheModule
import kt225.http.plugin.installCallLoggingPlugin
import kt225.http.routing.ApplicationRouting

/**
 * @author Jordan Abraham
 */
fun main(args: Array<String>) {
    try {
        val injector = Guice.createInjector(
            CacheModule,
            HttpModule(args)
        )

        val applicationEnvironment = injector.getInstance<ApplicationEnvironment>()
        val applicationEngine = injector.getInstance<ApplicationEngine>()
        val cache = injector.getInstance<Cache>()

        cache.release()
        System.gc()

        injector
            .findBindingsByType<ApplicationRouting>()
            .map { it.provider.get() }
            .forEach { it.route() }

        with(applicationEngine.application, Application::installCallLoggingPlugin)

        Runtime.getRuntime().addShutdownHook(ShutdownHook(applicationEnvironment.log, applicationEngine))
        applicationEngine.start(wait = true)
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}
