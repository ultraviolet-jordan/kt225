package kt225.http

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.commandLineEnvironment

/**
 * @author Jordan Abraham
 */
@Singleton
class ApplicationEnvironmentProvider @Inject constructor(
    private val applicationArguments: ApplicationArguments
) : Provider<ApplicationEnvironment> {
    override fun get(): ApplicationEnvironment = commandLineEnvironment(applicationArguments.args)
}
