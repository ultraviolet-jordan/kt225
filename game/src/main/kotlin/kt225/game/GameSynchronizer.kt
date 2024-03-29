package kt225.game

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.server.application.ApplicationEnvironment
import kt225.common.game.Synchronizer
import kt225.common.game.world.World
import kt225.game.synchronizer.task.EntitySynchronizer
import kt225.game.synchronizer.task.WorldSynchronizerTask
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

/**
 * @author Jordan Abraham
 */
@Singleton
class GameSynchronizer @Inject constructor(
    private val applicationEnvironment: ApplicationEnvironment,
    private val gameExecutor: ScheduledExecutorService,
    private val entityExecutor: ExecutorService,
    private val world: World,
    renderer: PlayerSynchronizerRenderer
) : Synchronizer {
    private var tick = 0

    private val tasks = setOf(
        WorldSynchronizerTask(world),
        EntitySynchronizer(world, renderer, entityExecutor)
    )

    override fun start() {
        world.start()
        gameExecutor.scheduleAtFixedRate(this, 0, 600, TimeUnit.MILLISECONDS)
    }

    override fun stop() {
        world.stop()
        entityExecutor.shutdown()
        gameExecutor.shutdown()
    }

    override fun run() {
        try {
            val time = measureTimeMillis {
                ++tick
                for (task in tasks) {
                    task.cycle(tick)
                }
            }

            val freeMemoryMB = ((Runtime.getRuntime().freeMemory() / 1024) / 1024).toFloat()
            val totalMemoryMB = ((Runtime.getRuntime().totalMemory() / 1024) / 1024).toFloat()
            val maxMemoryMB = ((Runtime.getRuntime().maxMemory() / 1024) / 1024).toFloat()
            val allocatedMemoryMB = (totalMemoryMB - freeMemoryMB)
            val freeMemory = (maxMemoryMB - allocatedMemoryMB)
            val usedPercentage = (allocatedMemoryMB / maxMemoryMB) * 100
            applicationEnvironment.log.info("Game Tick #$tick took $time ms. Memory=(Max: $maxMemoryMB MB Allocated: $allocatedMemoryMB MB Free: $freeMemory MB Used: $usedPercentage%)")
        } catch (exception: Exception) {
            applicationEnvironment.log.error(exception.stackTraceToString())
        }
    }
}
