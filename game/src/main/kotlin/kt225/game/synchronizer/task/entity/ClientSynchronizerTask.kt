package kt225.game.synchronizer.task.entity

import kotlinx.coroutines.Runnable
import kt225.common.game.entity.animator.type.Teleport
import kt225.common.game.entity.player.Player
import kt225.packet.type.server.PlayerInfoPacket

/**
 * @author Jordan Abraham
 */
class ClientSynchronizerTask(
    private val player: Player,
    private val highDefinitionRenders: Array<ByteArray?>,
    private val lowDefinitionRenders: Array<ByteArray?>
) : Runnable {
    override fun run() {
        val position = player.position
        val animator = player.animator
        val renderer = player.renderer

        if (player.mapSquareChanged) { // TODO Sort of temporary for now.
            animator.animate(
                Teleport(
                    rendering = renderer.needsRendering(),
                    x = position.x - position.zoneOriginX,
                    z = position.z - position.zoneOriginZ,
                    plane = position.plane
                )
            )
        }

        player.client.writePacket(PlayerInfoPacket(player, highDefinitionRenders, lowDefinitionRenders))
    }
}
