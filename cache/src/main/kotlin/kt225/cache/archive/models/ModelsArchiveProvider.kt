package kt225.cache.archive.models

import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.JagArchiveUnzipped

/**
 * @author Jordan Abraham
 */
@Singleton
class ModelsArchiveProvider : Provider<ModelsArchive> {
    override fun get(): ModelsArchive {
        val resource = javaClass.getResourceAsStream("/archives/models")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return ModelsArchive(JagArchiveUnzipped.decode(bytes, "models"))
    }
}
