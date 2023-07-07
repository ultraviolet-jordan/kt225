package kt225.http.routing.client

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class IndexResourceProvider : Provider<IndexResource> {
    override fun get(): IndexResource {
        return IndexResource(javaClass.getResourceAsStream("/index.html")!!.readAllBytes())
    }
}
