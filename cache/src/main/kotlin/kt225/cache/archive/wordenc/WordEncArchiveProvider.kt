package kt225.cache.archive.wordenc

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class WordEncArchiveProvider : Provider<WordEncArchive> {
    override fun get(): WordEncArchive = WordEncArchive()
}
