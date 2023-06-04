package kt225.http.routing.media

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.Cache

/**
 * @author Jordan Abraham
 */
@Singleton
class MediaResourceProvider @Inject constructor(
    private val cache: Cache
) : Provider<MediaResource> {
    override fun get(): MediaResource {
        val bytes = cache.getArchiveResource("media")!!
        return MediaResource(cache.crcs()[4], bytes)
    }
}
