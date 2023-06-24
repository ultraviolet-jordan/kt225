package kt225.http.routing.sounds

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.Cache

/**
 * @author Jordan Abraham
 */
@Singleton
class SoundsResourceProvider @Inject constructor(
    private val cache: Cache
) : Provider<SoundsResource> {
    override fun get(): SoundsResource {
        val bytes = cache.getJagFileBytes("sounds")!!
        return SoundsResource(cache.crcs[8], bytes)
    }
}
