package kt225.http.plugin

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.request.path
import org.slf4j.event.Level

/**
 * @author Jordan Abraham
 */
fun Application.installCallLoggingPlugin() {
    install(CallLogging) {
        level = Level.INFO
        filter { it.request.path().startsWith("/") }
    }
}
