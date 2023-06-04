package kt225.http

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.loadCommonConfiguration
import io.ktor.server.netty.NettyApplicationEngine

/**
 * @author Jordan Abraham
 */
@Singleton
class ApplicationEngineProvider @Inject constructor(
    private val applicationEnvironment: ApplicationEnvironment
) : Provider<ApplicationEngine> {
    override fun get(): ApplicationEngine = NettyApplicationEngine(applicationEnvironment as ApplicationEngineEnvironment) {
        loadCommonConfiguration(applicationEnvironment.config)
    }
}
