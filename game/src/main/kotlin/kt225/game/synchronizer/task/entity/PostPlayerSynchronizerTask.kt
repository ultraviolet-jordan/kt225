package kt225.game.synchronizer.task.entity

import kotlinx.coroutines.Runnable
import kt225.common.game.entity.player.Player
import kt225.game.PlayerSynchronizerRenderer

/**
 * @author Jordan Abraham
 */
class PostPlayerSynchronizerTask(
    private val player: Player,
    private val renderer: PlayerSynchronizerRenderer
) : Runnable {
    override fun run() {
        player.client.flushWriteQueue()
        player.renderer.clear()
        player.animator.clear()
        player.reset()
        renderer.clear()
    }
}
