package kt225.game

import com.google.inject.Provider
import com.google.inject.Singleton
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author Jordan Abraham
 */
@Singleton
class EntityPoolExecutorProvider : Provider<ExecutorService> {
    override fun get(): ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
}
