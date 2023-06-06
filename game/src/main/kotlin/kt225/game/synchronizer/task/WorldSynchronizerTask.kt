package kt225.game.synchronizer.task

import kt225.common.game.world.World
import kt225.game.synchronizer.SynchronizerTask

/**
 * @author Jordan Abraham
 */
class WorldSynchronizerTask(
    private val world: World
) : SynchronizerTask {
    override fun cycle(tick: Int) {
        world.processLoginRequests()
    }
}
