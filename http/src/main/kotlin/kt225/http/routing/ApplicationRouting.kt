package kt225.http.routing

import io.ktor.server.application.Application

/**
 * @author Jordan Abraham
 */
interface ApplicationRouting {
    fun route(application: Application)
}
