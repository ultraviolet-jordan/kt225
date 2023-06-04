package kt225.http.routing.models

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.Cache

/**
 * @author Jordan Abraham
 */
@Singleton
class ModelsResourceProvider @Inject constructor(
    private val cache: Cache
) : Provider<ModelsResource> {
    override fun get(): ModelsResource {
        val bytes = cache.getArchiveResource("models")!!
        return ModelsResource(cache.crcs()[5], bytes)
    }
}
