package kt225.http.routing.inter

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.Cache

/**
 * @author Jordan Abraham
 */
@Singleton
class InterfaceResourceProvider @Inject constructor(
    private val cache: Cache
) : Provider<InterfaceResource> {
    override fun get(): InterfaceResource {
        val bytes = cache.getArchiveResource("interface")!!
        return InterfaceResource(cache.crcs[3], bytes)
    }
}
