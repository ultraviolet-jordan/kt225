package kt225.cache.archive.inter

import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.JagArchive

/**
 * @author Jordan Abraham
 */
@Singleton
class InterfaceArchiveProvider : Provider<InterfaceArchive> {
    override fun get(): InterfaceArchive {
        val resource = javaClass.getResourceAsStream("/archives/interface")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return InterfaceArchive(JagArchive.decode(bytes))
    }
}
