package kt225.cache.archive.media

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class MediaArchiveProvider : Provider<MediaArchive> {
    override fun get(): MediaArchive = MediaArchive()
}
