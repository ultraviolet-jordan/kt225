package kt225.http.routing.config

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.Cache

/**
 * @author Jordan Abraham
 */
@Singleton
class ConfigResourceProvider @Inject constructor(
    private val cache: Cache
) : Provider<ConfigResource> {
    override fun get(): ConfigResource {
        val bytes = cache.getArchiveResource("config")!!
        return ConfigResource(cache.crcs()[2], bytes)
    }
}
