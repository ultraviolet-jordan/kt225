package kt225.http.routing.songs

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.Cache

/**
 * @author Jordan Abraham
 */
@Singleton
class SongsResourceProvider @Inject constructor(
    private val cache: Cache
) : Provider<SongsResourceList> {
    override fun get(): SongsResourceList = SongsResourceList(cache.songs.map { SongsResource(it.key, it.value) })
}
