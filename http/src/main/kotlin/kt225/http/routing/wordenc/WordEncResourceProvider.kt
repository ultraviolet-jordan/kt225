package kt225.http.routing.wordenc

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.Cache

/**
 * @author Jordan Abraham
 */
@Singleton
class WordEncResourceProvider @Inject constructor(
    private val cache: Cache
) : Provider<WordEncResource> {
    override fun get(): WordEncResource {
        val bytes = cache.getArchiveResource("wordenc")!!
        return WordEncResource(cache.crcs[7], bytes)
    }
}
