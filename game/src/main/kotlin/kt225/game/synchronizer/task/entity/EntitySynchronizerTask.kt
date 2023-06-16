package kt225.game.synchronizer.task.entity

import java.util.concurrent.Phaser

/**
 * @author Jordan Abraham
 */
class EntitySynchronizerTask(
    private val phaser: Phaser,
    private val task: Runnable
) : Runnable {
    override fun run() {
        try {
            task.run()
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            phaser.arriveAndDeregister()
        }
    }
}
