package kt225.application

import io.ktor.application.Application
import io.ktor.server.engine.commandLineEnvironment
import kt225.cache.archive.type.obj.ObjTypeLoader
import kt225.cache.cacheModule
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.modules
import java.util.TimeZone

/**
 * @author Jordan Abraham
 */
fun main(args: Array<String>) = commandLineEnvironment(args).start()

fun Application.module() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    installKoin()

    val objs by inject<ObjTypeLoader>()
    objs.entries.forEach(::println)
}

fun Application.installKoin() {
    modules(
        cacheModule
    )
}
