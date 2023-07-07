package kt225.http.routing.client

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.Routing
import kt225.http.routing.ApplicationRouting

/**
 * @author Jordan Abraham
 */
@Singleton
class ClientRouting @Inject constructor(
    private val routing: Routing
) : ApplicationRouting {
    override fun route() {
        routing.staticResources("/js", "client/js")
        routing.staticResources("/SCC1_Florestan.sf2", "client")
        routing.staticResources("/client", "client")
    }
}
