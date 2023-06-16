package kt225.http

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.findBindingsByType
import dev.misfitlabs.kotlinguice4.getInstance
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine
import kt225.cache.CacheModule
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

        Runtime.getRuntime().addShutdownHook(ShutdownHook(applicationEnvironment.log, applicationEngine))

        injector
            .findBindingsByType<ApplicationRouting>()
            .map { it.provider.get() }
            .forEach { it.route(applicationEngine.application) }

        applicationEngine.start(wait = true)
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}
