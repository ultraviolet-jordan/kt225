package kt225.cache.archive.sounds

import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.JagArchive

/**
 * @author Jordan Abraham
 */
@Singleton
class SoundsArchiveProvider : Provider<SoundsArchive> {
    override fun get(): SoundsArchive {
        val resource = javaClass.getResourceAsStream("/archives/sounds")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return SoundsArchive(JagArchive.decode(bytes))
    }
}
