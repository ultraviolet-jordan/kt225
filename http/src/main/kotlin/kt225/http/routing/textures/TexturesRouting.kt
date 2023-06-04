package kt225.http.routing.textures

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kt225.http.routing.ApplicationRouting

/**
 * @author Jordan Abraham
 */
@Singleton
class TexturesRouting @Inject constructor(
    private val texturesResource: TexturesResource
) : ApplicationRouting {
    override fun route(application: Application) {
        application.routing {
            get("/textures${texturesResource.crc}") {
                call.respondBytes(texturesResource.bytes)
            }
        }
    }
}
