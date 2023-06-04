package kt225.http.routing.wordenc

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
class WordEncRouting @Inject constructor(
    private val wordEncResource: WordEncResource
) : ApplicationRouting {
    override fun route(application: Application) {
        application.routing {
            get("/wordenc${wordEncResource.crc}") {
                call.respondBytes(wordEncResource.bytes)
            }
        }
    }
}
