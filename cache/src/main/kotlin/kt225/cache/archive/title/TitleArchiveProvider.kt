package kt225.cache.archive.title

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class TitleArchiveProvider : Provider<TitleArchive> {
    override fun get(): TitleArchive = TitleArchive()
}
