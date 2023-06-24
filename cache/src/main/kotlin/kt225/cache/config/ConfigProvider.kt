package kt225.cache.config

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class ConfigProvider : Provider<Config> {
    override fun get(): Config {
        val resource = javaClass.getResourceAsStream("/archives/config")!!
        val bytes = resource.readAllBytes()
        resource.close()
        return Config(bytes).unpack()
    }
}
