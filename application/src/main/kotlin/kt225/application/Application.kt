package kt225.application

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.server.engine.commandLineEnvironment
import kt225.cache.cacheModule
import kt225.cache.installHttpServer
import org.koin.ktor.ext.Koin
import java.util.TimeZone

/**
 * @author Jordan Abraham
 */
fun main(args: Array<String>) = commandLineEnvironment(args).start()

fun Application.module() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    installKoin()
    installHttpServer()
}

fun Application.installKoin() {
    install(Koin) {
        modules(
            cacheModule
        )
    }
}
