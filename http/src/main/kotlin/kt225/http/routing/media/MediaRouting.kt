package kt225.http.routing.media

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
class MediaRouting @Inject constructor(
    private val mediaResource: MediaResource
) : ApplicationRouting {
    override fun route(application: Application) {
        application.routing {
            get("/media${mediaResource.crc}") {
                call.respondBytes(mediaResource.bytes)
            }
        }
    }
}
