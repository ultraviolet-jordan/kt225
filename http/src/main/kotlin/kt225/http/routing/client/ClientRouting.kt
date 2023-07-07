package kt225.http.routing.client

import com.google.inject.Singleton
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing
import kt225.http.routing.ApplicationRouting

/**
 * @author Jordan Abraham
 */
@Singleton
class ClientRouting : ApplicationRouting {
    override fun route(application: Application) {
        application.routing {
            staticResources("/js", "client/js")
            staticResources("/SCC1_Florestan.sf2", "client")
            staticResources("/client", "client")
        }
    }
}
