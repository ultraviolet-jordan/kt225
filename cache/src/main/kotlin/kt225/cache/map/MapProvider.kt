package kt225.cache.map

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
class MapProvider : Provider<Maps> {
    override fun get(): Maps = Maps().also {
        val uri = javaClass.getResource("/maps/")!!.toURI()
        val start = try {
            Paths.get(uri)
        } catch (exception: FileSystemNotFoundException) {
            FileSystems.newFileSystem(uri, HashMap<String, String>()).getPath("/maps/")
        }
        Files.walk(start).forEach { path ->
            val name = path.fileName.toString()
            if (!name.startsWith("m") && !name.startsWith("l")) {
                return@forEach
            }

            val stream = javaClass.getResourceAsStream("/maps/${path.fileName}") ?: return@forEach
            val bytes = stream.readAllBytes()
            stream.close()
            val crc32 = CRC32()
            crc32.update(bytes)

            val type = if (name.first().toString() == "m") 0 else 1
            val pos = name.drop(1).split("_")
            val x = pos.first().toInt()
            val z = pos.last().toInt()

            val resource = MapResource(
                name = name,
                id = x shl 8 or z,
                x = x,
                z = z,
                type = type,
                bytes = bytes,
                crc = crc32.value.toInt()
            )

            it.add(resource)
        }
    }
}
