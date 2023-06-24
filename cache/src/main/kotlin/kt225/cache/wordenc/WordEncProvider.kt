package kt225.cache.wordenc

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class WordEncProvider : Provider<WordEnc> {
    override fun get(): WordEnc {
        val resource = javaClass.getResourceAsStream("/archives/wordenc")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return WordEnc(bytes).unpack()
    }
}
