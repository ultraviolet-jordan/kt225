package kt225.cache.textures

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class TexturesProvider : Provider<Textures> {
    override fun get(): Textures {
        val resource = javaClass.getResourceAsStream("/archives/textures")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return Textures(bytes).unpack()
    }
}
