package kt225.game.synchronizer.task.entity

import kotlinx.coroutines.Runnable
import kt225.common.game.entity.player.Player
import kt225.packet.type.server.PlayerInfoPacket

/**
 * @author Jordan Abraham
 */
class ClientSynchronizerTask(
    private val player: Player
) : Runnable {
    override fun run() {
        player.client.writePacket(PlayerInfoPacket(player.index, player.viewport))
    }
}
