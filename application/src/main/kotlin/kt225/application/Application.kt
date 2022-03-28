package kt225.application

import io.ktor.application.Application
import io.ktor.server.engine.commandLineEnvironment
import kt225.cache.archive.type.obj.ObjTypeLoader
import kt225.cache.archive.type.seq.SeqTypeLoader
import kt225.cache.cacheModule
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.modules
import java.util.TimeZone
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * @author Jordan Abraham
 */
fun main(args: Array<String>) = commandLineEnvironment(args).start()

@OptIn(ExperimentalTime::class)
fun Application.module() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    installKoin()

    val time = measureTime {
        val objs by inject<ObjTypeLoader>()
        println("Loaded ${objs.size()} objs.")

        val seqs by inject<SeqTypeLoader>()
        println("Loaded ${seqs.size()} seqs.")
    }
    println("Finished in $time.")
}

fun Application.installKoin() {
    modules(
        cacheModule
    )
}
