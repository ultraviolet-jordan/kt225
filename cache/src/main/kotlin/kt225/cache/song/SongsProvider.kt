package kt225.cache.song

import com.google.inject.Provider
import com.google.inject.Singleton
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
@Singleton
class SongsProvider : Provider<Songs> {
    override fun get(): Songs = Songs().also {
        val uri = javaClass.getResource("/songs/")!!.toURI()
        val start = try {
            Paths.get(uri)
        } catch (exception: FileSystemNotFoundException) {
            FileSystems.newFileSystem(uri, HashMap<String, String>()).getPath("/songs/")
        }
        Files.walk(start).forEach { path ->
            val stream = javaClass.getResourceAsStream("/songs/${path.fileName}") ?: return@forEach
            val bytes = stream.readAllBytes()
            stream.close()
            val crc32 = CRC32()
            crc32.update(bytes)
            it.add(SongResource(path.fileName.toString().take(10), bytes, crc32.value.toInt()))
        }
    }
}
