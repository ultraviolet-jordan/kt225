package kt225.http.routing.title

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.Cache

/**
 * @author Jordan Abraham
 */
@Singleton
class TitleResourceProvider @Inject constructor(
    private val cache: Cache
) : Provider<TitleResource> {
    override fun get(): TitleResource {
        val bytes = cache.getJagFileBytes("title")!!
        return TitleResource(cache.crcs[1], bytes)
    }
}
