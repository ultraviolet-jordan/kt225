package kt225.cache

import bzip2.BZip2InputStream
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
import kt225.cache.archive.Config
import kt225.cache.archive.Interface
import kt225.cache.archive.Media
import kt225.cache.archive.Models
import kt225.cache.archive.Sounds
import kt225.cache.archive.Textures
import kt225.cache.archive.Title
import kt225.cache.archive.WordEnc
import kt225.cache.type.config.flo.FloTypeLoader
import kt225.cache.type.config.loc.LocTypeLoader
import kt225.cache.type.config.npc.NpcTypeLoader
import kt225.cache.type.config.obj.ObjTypeLoader
import kt225.cache.type.config.seq.SeqTypeLoader
import kt225.cache.type.config.spotanim.SpotAnimTypeLoader
import kt225.cache.type.config.varp.VarpTypeLoader
import kt225.cache.type.map.MapTypeLoader
import org.koin.dsl.module
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Jordan Abraham
 */
val cacheModule = module(createdAtStart = true) {
    single { SeqTypeLoader() }
    single { ObjTypeLoader() }
    single { NpcTypeLoader() }
    single { LocTypeLoader() }
    single { MapTypeLoader() }
    single { SpotAnimTypeLoader() }
    single { VarpTypeLoader() }
    single { FloTypeLoader() }
}

/**
 * The cache archives crcs.
 */
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

/**
 * The cache songs.
 */
private val songs by lazy(::songsResource)

/**
 * The cache maps.
 */
internal val maps by lazy(::mapsResource)

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
            get("/title${crcs[1]}") {
                call.respondBytes { Title.archive.bytes() }
            }
            get("/config${crcs[2]}") {
                call.respondBytes { Config.archive.bytes() }
            }
            get("/interface${crcs[3]}") {
                call.respondBytes { Interface.archive.bytes() }
            }
            get("/media${crcs[4]}") {
                call.respondBytes { Media.archive.bytes() }
            }
            get("/models${crcs[5]}") {
                call.respondBytes { Models.archive.bytes() }
            }
            get("/textures${crcs[6]}") {
                call.respondBytes { Textures.archive.bytes() }
            }
            get("/wordenc${crcs[7]}") {
                call.respondBytes { WordEnc.archive.bytes() }
            }
            get("/sounds${crcs[8]}") {
                println("hello world")
                call.respondBytes { Sounds.archive.bytes() }
            }
            get("/songs/{file}") {
                // The song file request from the client.
                val file = call.parameters["file"] ?: return@get
                // Take the first 10 characters to find the song file in our resources.
                val song = songs[file.take(10)] ?: return@get
                // TODO Validate crc for songs ?
                call.respondBytes { song }
            }
        }
    }.start(wait = false)
}

/**
 * Gets a cache archive file from the resource directory.
 */
internal fun archiveResource(name: String) = object {}.javaClass.getResourceAsStream("/archives/$name")!!

/**
 * Gets all the song files from the resource directory.
 */
internal fun songsResource() = buildMap {
    Files.walk(Paths.get(object {}.javaClass.getResource("/songs/")!!.toURI())).forEach {
        val stream = object {}.javaClass.getResourceAsStream("/songs/${it.fileName}") ?: return@forEach
        put(it.fileName.toString().take(10), stream.readAllBytes())
    }
}

/**
 * Gets all the map files from the resource directory.
 */
internal fun mapsResource() = buildMap {
    Files.walk(Paths.get(object {}.javaClass.getResource("/maps/")!!.toURI())).forEach {
        val stream = object {}.javaClass.getResourceAsStream("/maps/${it.fileName}") ?: return@forEach
        put(it.fileName.toString(), stream.readAllBytes())
    }
}

fun ByteArray.decompressBzip2(dest: ByteArray, destLength: Int, srcLength: Int, srcOffset: Int) {
    BZip2InputStream.read(dest, destLength, this, srcLength, srcOffset)
}
