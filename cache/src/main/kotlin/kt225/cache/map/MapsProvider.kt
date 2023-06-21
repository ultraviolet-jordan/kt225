package kt225.cache.map

import com.google.inject.Provider
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
interface MapsProvider<T : MutableList<MapResource>> : Provider<T> {
    fun prefix(): String
    fun resources(): T
    
    override fun get(): T {
        val list = resources()
        val uri = javaClass.getResource("/maps/")!!.toURI()
        val start = try {
            Paths.get(uri)
        } catch (exception: FileSystemNotFoundException) {
            FileSystems.newFileSystem(uri, HashMap<String, String>()).getPath("/maps/")
        }
        Files.walk(start).forEach { path ->
            val name = path.fileName.toString()
            if (!name.startsWith(prefix()) || list.any { it.name == name }) {
                return@forEach
            }

            val stream = javaClass.getResourceAsStream("/maps/${path.fileName}") ?: return@forEach
            val bytes = stream.readAllBytes()
            stream.close()
            val crc32 = CRC32()
            crc32.update(bytes)

            val pos = name.drop(1).split("_")
            val x = pos.first().toInt()
            val z = pos.last().toInt()

            val resource = MapResource(
                name = name,
                id = x shl 8 or z,
                x = x,
                z = z,
                bytes = bytes,
                crc = crc32.value.toInt()
            )

            list.add(resource)
        }
        return list
    }
}
