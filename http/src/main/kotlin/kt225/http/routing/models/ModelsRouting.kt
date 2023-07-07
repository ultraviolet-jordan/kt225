package kt225.http.routing.models

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
class ModelsRouting @Inject constructor(
    private val routing: Routing,
    private val modelsResource: ModelsResource
) : ApplicationRouting {
    override fun route() {
        routing.get("/models${modelsResource.crc}") {
            call.respondBytes(modelsResource.bytes)
        }
    }
}
