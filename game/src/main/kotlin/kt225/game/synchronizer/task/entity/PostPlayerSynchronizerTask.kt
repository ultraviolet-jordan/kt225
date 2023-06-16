package kt225.game.synchronizer.task.entity

import kotlinx.coroutines.Runnable
import kt225.common.game.entity.player.Player

/**
 * @author Jordan Abraham
 */
class PostPlayerSynchronizerTask(
    private val player: Player
) : Runnable {
    override fun run() {
        player.client.flushWriteQueue()
        player.renderer.clear()
        player.animator.clear()
        player.reset()
    }
}
