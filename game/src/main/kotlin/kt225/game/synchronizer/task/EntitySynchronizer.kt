package kt225.game.synchronizer.task

import kt225.common.game.entity.player.Player
import kt225.common.game.synchronizer.SynchronizerTask
import kt225.common.game.world.World
import kt225.game.PlayerSynchronizerRenderer
import kt225.game.synchronizer.task.entity.ClientSynchronizerTask
import kt225.game.synchronizer.task.entity.EntitySynchronizerTask
import kt225.game.synchronizer.task.entity.PostPlayerSynchronizerTask
import kt225.game.synchronizer.task.entity.PrePlayerSynchronizerTask
import java.util.concurrent.ExecutorService
import java.util.concurrent.Phaser

/**
 * @author Jordan Abraham
 */
class EntitySynchronizer(
    private val world: World,
    private val renderer: PlayerSynchronizerRenderer,
    private val executor: ExecutorService
) : SynchronizerTask {
    private val phaser = Phaser(1)
    private val players get() = world.players.toList().filterNotNull().filter(Player::online)

    override fun cycle(tick: Int) {
        val players = players
        
        phaser.bulkRegister(players.size)
        for (player in players) {
            val task = PrePlayerSynchronizerTask(player, renderer)
            executor.submit(EntitySynchronizerTask(phaser, task))
        }
        phaser.arriveAndAwaitAdvance()

        phaser.bulkRegister(players.size)
        for (player in players) {
            val task = ClientSynchronizerTask(player)
            executor.submit(EntitySynchronizerTask(phaser, task))
        }
        phaser.arriveAndAwaitAdvance()

        phaser.bulkRegister(players.size)
        for (player in players) {
            val task = PostPlayerSynchronizerTask(player, renderer)
            executor.submit(EntitySynchronizerTask(phaser, task))
        }
        phaser.arriveAndAwaitAdvance()
    }
}
