package kt225.cache.media

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class MediaProvider : Provider<Media> {
    override fun get(): Media {
        val resource = javaClass.getResourceAsStream("/archives/media")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return Media(bytes).unpack()
    }
}
