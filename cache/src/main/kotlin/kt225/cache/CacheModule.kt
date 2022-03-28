package kt225.cache

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondBytes
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeInt
import kt225.cache.archive.Archive
import kt225.cache.archive.Config
import kt225.cache.archive.Interface
import kt225.cache.archive.Media
import kt225.cache.archive.Models
import kt225.cache.archive.Sounds
import kt225.cache.archive.Textures
import kt225.cache.archive.Title
import kt225.cache.archive.WordEnc
import kt225.cache.archive.type.obj.ObjTypeLoader
import kt225.cache.archive.type.seq.SeqTypeLoader
import org.koin.dsl.module

/**
 * @author Jordan Abraham
 */
val cacheModule = module(createdAtStart = true) {
    single { SeqTypeLoader() }
    single { ObjTypeLoader() }
}

private val crcs = arrayOf(
    0,
    Title.archive.crc(),
    Config.archive.crc(),
    Interface.archive.crc(),
    Media.archive.crc(),
    Models.archive.crc(),
    Textures.archive.crc(),
    WordEnc.archive.crc(),
    Sounds.archive.crc()
)

fun Application.installHttpServer() {
    embeddedServer(Netty, port = 80) {
        routing {
            get("/crc{id}") {
                val buffer = buildPacket {
                    crcs.forEach(::writeInt)
                }
                call.respondBytes { buffer.readBytes() }
                buffer.release()
            }
            get("/title${Title.archive.crc()}") {
                call.respondBytes { Title.archive.bytes() }
            }
            get("/config${Config.archive.crc()}") {
                call.respondBytes { Config.archive.bytes() }
            }
            get("/interface${Interface.archive.crc()}") {
                call.respondBytes { Interface.archive.bytes() }
            }
            get("/media${Media.archive.crc()}") {
                call.respondBytes { Media.archive.bytes() }
            }
            get("/models${Models.archive.crc()}") {
                call.respondBytes { Models.archive.bytes() }
            }
            get("/textures${Textures.archive.crc()}") {
                call.respondBytes { Textures.archive.bytes() }
            }
            get("/wordenc${WordEnc.archive.crc()}") {
                call.respondBytes { WordEnc.archive.bytes() }
            }
            get("/sounds${Sounds.archive.crc()}") {
                call.respondBytes { Sounds.archive.bytes() }
            }
        }
    }.start(wait = false)
}

internal fun resource(name: String) = Archive::class.java.getResourceAsStream("/archives/$name")!!
