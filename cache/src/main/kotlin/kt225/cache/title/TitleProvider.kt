package kt225.cache.title

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class TitleProvider : Provider<Title> {
    override fun get(): Title {
        val resource = javaClass.getResourceAsStream("/archives/title")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return Title(bytes)
    }
}
