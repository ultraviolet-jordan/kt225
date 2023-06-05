package kt225.game.sync.task

import kt225.common.game.world.World
import kt225.game.sync.SynchronizerTask
import kt225.packet225.type.server.PlayerInfoPacket

/**
 * @author Jordan Abraham
 */
class WorldSynchronizerTask(
    private val world: World
) : SynchronizerTask {
    override fun cycle(tick: Int) {
        world.processLoginRequests()

        val players = world.players()
        for (player in players) {
            player.client.writePacket(PlayerInfoPacket())
            player.client.flushWriteQueue()
        }
    }
}
