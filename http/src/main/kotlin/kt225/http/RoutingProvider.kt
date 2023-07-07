package kt225.http

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.routing.Routing
import io.ktor.server.routing.routing

/**
 * @author Jordan Abraham
 */
@Singleton
class RoutingProvider @Inject constructor(
    private val applicationEngine: ApplicationEngine
) : Provider<Routing> {
    override fun get(): Routing {
        return applicationEngine.application.routing {}
    }
}
