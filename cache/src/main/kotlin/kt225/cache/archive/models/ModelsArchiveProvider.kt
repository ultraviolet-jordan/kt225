package kt225.cache.archive.models

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class ModelsArchiveProvider : Provider<ModelsArchive> {
    override fun get(): ModelsArchive = ModelsArchive()
}
