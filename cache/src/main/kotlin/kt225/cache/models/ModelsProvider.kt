package kt225.cache.models

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class ModelsProvider : Provider<Models> {
    override fun get(): Models {
        val resource = javaClass.getResourceAsStream("/archives/models")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return Models(bytes).unpack()
    }
}
