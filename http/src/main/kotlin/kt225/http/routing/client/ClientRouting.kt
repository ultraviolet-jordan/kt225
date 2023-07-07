package kt225.http.routing.client

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
class ClientRouting @Inject constructor(
    private val indexResource: IndexResource
) : ApplicationRouting {
    
    override fun route(application: Application) {
        application.routing { 
            get("/client") {
                call.respondBytes(indexResource.bytes)
            }
            get("/js/teavm/classes.js") {
                call.respondBytes(javaClass.getResourceAsStream("/classes.js")!!.readAllBytes())
            }
            get("/js/midi.js") {
                call.respondBytes(javaClass.getResourceAsStream("/midi.js")!!.readAllBytes())
            }
        }
    }
}
