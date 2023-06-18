package kt225.cache.archive.config

import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.JagArchive

/**
 * @author Jordan Abraham
 */
@Singleton
class ConfigArchiveProvider : Provider<ConfigArchive> {
    override fun get(): ConfigArchive {
        val resource = javaClass.getResourceAsStream("/archives/config")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return ConfigArchive(JagArchive.decode(bytes))
    }
}
