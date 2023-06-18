package kt225.cache.archive.textures

import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.JagArchive

/**
 * @author Jordan Abraham
 */
@Singleton
class TexturesArchiveProvider : Provider<TexturesArchive> {
    override fun get(): TexturesArchive {
        val resource = javaClass.getResourceAsStream("/archives/textures")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return TexturesArchive(JagArchive.decode(bytes))
    }
}
