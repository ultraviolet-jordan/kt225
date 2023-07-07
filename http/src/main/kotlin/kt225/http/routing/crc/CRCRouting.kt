package kt225.http.routing.crc

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kt225.cache.Cache
import kt225.http.routing.ApplicationRouting
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class CRCRouting @Inject constructor(
    private val routing: Routing,
    private val cache: Cache
) : ApplicationRouting {
    override fun route() {
        routing.get("/crc{id}") {
            val crcs = cache.crcs
            val buffer = ByteBuffer.allocate(crcs.size * 4)
            crcs.forEach(buffer::putInt)
            if (buffer.hasArray()) {
                call.respondBytes(buffer.array())
            } else {
                call.respondBytes(ByteArray(buffer.position()) { buffer.get() })
            }
        }
    }
}
