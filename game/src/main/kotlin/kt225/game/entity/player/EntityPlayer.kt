package kt225.game.entity.player

import kt225.common.game.Client
import kt225.common.game.entity.player.Player
import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
class EntityPlayer(
    client: Client,
    world: World
) : Player(client, world) {
    override fun init() {
    }

    override fun login() {
        client.flushWriteChannel()
    }
}
