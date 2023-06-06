package kt225.game.synchronizer.task

import kt225.common.game.world.World
import kt225.game.PlayerSynchronizerRenderer
import kt225.game.synchronizer.SynchronizerTask
import kt225.packet225.type.server.PlayerInfoPacket

/**
 * @author Jordan Abraham
 */
class WorldSynchronizerTask(
    private val world: World,
    private val playerSynchronizerRenderer: PlayerSynchronizerRenderer
) : SynchronizerTask {
    override fun cycle(tick: Int) {
        world.processLoginRequests()

        val players = world.players()

        for (player in players) {
            if (player == null || !player.online()) {
                continue
            }
            playerSynchronizerRenderer.renderEntity(player)
        }

        val highDefinitionRenders = playerSynchronizerRenderer.highDefinitionRenders
        val lowDefinitionRenders = playerSynchronizerRenderer.lowDefinitionRenders

        for (player in players) {
            if (player == null || !player.online()) {
                continue
            }
            val client = player.client
            client.writePacket(PlayerInfoPacket(player, players, highDefinitionRenders, lowDefinitionRenders))
            player.renderer().clear()
            client.flushWriteQueue()
            player.needsPlacement = false // TODO This is temporary.
        }

        playerSynchronizerRenderer.clear()
    }
}
