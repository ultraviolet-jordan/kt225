package kt225.http.routing.textures

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.Cache

/**
 * @author Jordan Abraham
 */
@Singleton
class TexturesResourceProvider @Inject constructor(
    private val cache: Cache
) : Provider<TexturesResource> {
    override fun get(): TexturesResource {
        val bytes = cache.getArchiveResource("textures")!!
        return TexturesResource(cache.crcs[6], bytes)
    }
}
