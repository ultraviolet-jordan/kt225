package kt225.game.synchronizer.task

import kt225.common.game.entity.animator.type.Teleport
import kt225.common.game.entity.render.type.Appearance
import kt225.common.game.world.World
import kt225.game.PlayerSynchronizerRenderer
import kt225.game.synchronizer.SynchronizerTask
import kt225.packet225.type.server.PlayerInfoPacket

/**
 * @author Jordan Abraham
 */
class PlayerSynchronizerTask(
    private val world: World,
    private val playerSynchronizerRenderer: PlayerSynchronizerRenderer
) : SynchronizerTask {
    override fun cycle(tick: Int) {
        val players = world.players().toList()

        // Check for players logging into the world first.
        players.parallelStream().forEach { player ->
            // Skip players already online.
            if (player == null || player.online) {
                return@forEach
            }
            player.renderer().render(
                Appearance(
                    headIcon = 0,
                    name = "Jordan",
                    combatLevel = 44
                )
            )
            player.animator().animate(
                Teleport(
                    rendering = true,
                    x = player.position.x - player.position.zoneOriginX,
                    z = player.position.z - player.position.zoneOriginZ,
                    plane = player.position.plane
                )
            )
            player.online = true
        }

        players.parallelStream().forEach { player ->
            if (player == null || !player.online) {
                return@forEach
            }
            playerSynchronizerRenderer.renderEntity(player)
        }

        val highDefinitionRenders = playerSynchronizerRenderer.highDefinitionRenders
        val lowDefinitionRenders = playerSynchronizerRenderer.lowDefinitionRenders

        players.parallelStream().forEach { player ->
            if (player == null || !player.online) {
                return@forEach
            }
            val client = player.client
            client.writePacket(PlayerInfoPacket(player, highDefinitionRenders, lowDefinitionRenders))
            client.flushWriteQueue()
        }

        players.parallelStream().forEach { player ->
            if (player == null || !player.online) {
                return@forEach
            }
            player.renderer().clear()
            player.animator().clear()
        }

        playerSynchronizerRenderer.clear()
    }
}
