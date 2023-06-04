package kt225.http.routing.songs

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
class SongsRouting @Inject constructor(
    private val songsResourceList: SongsResourceList
) : ApplicationRouting {
    override fun route(application: Application) {
        application.routing {
            get("/songs/{file}") {
                // The song file request from the client.
                val file = call.parameters["file"] ?: return@get
                // Take the first 10 characters to find the song file in our resources.
                val song = songsResourceList.firstOrNull { it.name == file.take(10) } ?: return@get
                call.respondBytes { song.bytes }
            }
        }
    }
}
