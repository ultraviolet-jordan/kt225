package kt225.cache.archive.sounds

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class SoundsArchiveProvider : Provider<SoundsArchive> {
    override fun get(): SoundsArchive = SoundsArchive()
}
