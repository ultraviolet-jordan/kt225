package kt225.cache.archive.media

import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.JagArchive

/**
 * @author Jordan Abraham
 */
@Singleton
class MediaArchiveProvider : Provider<MediaArchive> {
    override fun get(): MediaArchive {
        val resource = javaClass.getResourceAsStream("/archives/media")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return MediaArchive(JagArchive.decode(bytes))
    }
}
