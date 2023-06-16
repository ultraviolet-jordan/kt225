package kt225.game.synchronizer.task.entity

import kotlinx.coroutines.Runnable
import kt225.common.game.entity.player.Player
import kt225.game.PlayerSynchronizerRenderer

/**
 * @author Jordan Abraham
 */
class PrePlayerSynchronizerTask(
    private val player: Player,
    private val renderer: PlayerSynchronizerRenderer
) : Runnable {
    override fun run() {
        renderer.renderEntity(player)
    }
}
