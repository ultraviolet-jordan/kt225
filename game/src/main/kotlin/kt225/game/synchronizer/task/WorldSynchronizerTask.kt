package kt225.game.synchronizer.task

import kt225.common.game.synchronizer.SynchronizerTask
import kt225.common.game.world.World

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
