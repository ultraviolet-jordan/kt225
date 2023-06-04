package kt225.cache.archive.config

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class ConfigArchiveProvider : Provider<ConfigArchive> {
    override fun get(): ConfigArchive = ConfigArchive()
}
