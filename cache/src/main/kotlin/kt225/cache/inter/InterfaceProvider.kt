package kt225.cache.inter

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class InterfaceProvider : Provider<Interface> {
    override fun get(): Interface {
        val resource = javaClass.getResourceAsStream("/archives/interface")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return Interface(bytes).unpack()
    }
}
