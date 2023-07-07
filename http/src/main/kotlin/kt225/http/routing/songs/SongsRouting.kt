package kt225.http.routing.songs

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kt225.cache.song.Songs
import kt225.http.routing.ApplicationRouting

/**
 * @author Jordan Abraham
 */
@Singleton
class SongsRouting @Inject constructor(
    private val routing: Routing,
    private val songs: Songs
) : ApplicationRouting {
    override fun route() {
        routing.get("/songs/{file}") {
            // The song file request from the client.
            val file = call.parameters["file"] ?: return@get
            // Take the first 10 characters to find the song file in our resources.
            val song = songs.firstOrNull { it.name == file.take(10) } ?: return@get
            call.respondBytes { song.bytes }
        }
    }
}
