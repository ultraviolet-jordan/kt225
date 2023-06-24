package kt225.cache.sounds

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class SoundsProvider : Provider<Sounds> {
    override fun get(): Sounds {
        val resource = javaClass.getResourceAsStream("/archives/sounds")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return Sounds(bytes).unpack()
    }
}
