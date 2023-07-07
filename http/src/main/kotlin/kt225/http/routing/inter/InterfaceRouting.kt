package kt225.http.routing.inter

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kt225.http.routing.ApplicationRouting

/**
 * @author Jordan Abraham
 */
@Singleton
class InterfaceRouting @Inject constructor(
    private val routing: Routing,
    private val interfaceResource: InterfaceResource
) : ApplicationRouting {
    override fun route() {
        routing.get("/interface${interfaceResource.crc}") {
            call.respondBytes(interfaceResource.bytes)
        }
    }
}
