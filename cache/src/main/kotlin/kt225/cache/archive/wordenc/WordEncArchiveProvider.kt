package kt225.cache.archive.wordenc

import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.JagArchive

/**
 * @author Jordan Abraham
 */
@Singleton
class WordEncArchiveProvider : Provider<WordEncArchive> {
    override fun get(): WordEncArchive {
        val resource = javaClass.getResourceAsStream("/archives/wordenc")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return WordEncArchive(JagArchive.decode(bytes))
    }
}
