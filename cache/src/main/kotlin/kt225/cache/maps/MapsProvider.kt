package kt225.cache.maps

import com.google.inject.Provider
import com.google.inject.Singleton
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.util.HashMap
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
@Singleton
class MapsProvider : Provider<Maps> {
    override fun get(): Maps = Maps().also {
        val songsURI = javaClass.getResource("/maps/")!!.toURI()
        val songsPath = try {
            Paths.get(songsURI)
        } catch (exception: FileSystemNotFoundException) {
            FileSystems.newFileSystem(songsURI, HashMap<String, String>()).getPath("/maps/")
        }
        Files.walk(songsPath).forEach { path ->
            val resource = javaClass.getResourceAsStream("/maps/${path.fileName}") ?: return@forEach
            val bytes = resource.readAllBytes()
            resource.close()
            val crc32 = CRC32()
            crc32.update(bytes)
            it.add(MapResource(path.fileName.toString(), bytes, crc32.value.toInt()))
        }
    }
}
