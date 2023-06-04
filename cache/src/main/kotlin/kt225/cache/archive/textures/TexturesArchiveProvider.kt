package kt225.cache.archive.textures

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class TexturesArchiveProvider : Provider<TexturesArchive> {
    override fun get(): TexturesArchive = TexturesArchive()
}
