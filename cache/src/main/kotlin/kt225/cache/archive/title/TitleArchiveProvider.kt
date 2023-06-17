package kt225.cache.archive.title

import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.JagArchiveUnzipped

/**
 * @author Jordan Abraham
 */
@Singleton
class TitleArchiveProvider : Provider<TitleArchive> {
    override fun get(): TitleArchive {
        val resource = javaClass.getResourceAsStream("/archives/title")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return TitleArchive(JagArchiveUnzipped.decode(bytes, "title"))
    }
}
