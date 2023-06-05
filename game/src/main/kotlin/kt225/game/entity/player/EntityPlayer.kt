package kt225.game.entity.player

import kt225.common.game.Client
import kt225.common.game.entity.player.Player
import kt225.common.game.world.World
import kt225.packet225.type.server.LoadAreaPacket

/**
 * @author Jordan Abraham
 */
class EntityPlayer(
    client: Client,
    world: World
) : Player(client, world) {
    private var online = false

    override fun init() {
    }

    override fun online(): Boolean = online

    override fun login() {
        client.flushWriteChannel()

        val x = 3222
        val z = 3222
        val zoneX = x shr 3
        val zoneZ = z shr 3
        client.writePacket(LoadAreaPacket(zoneX, zoneZ))
        client.flushWriteQueue()
        this.online = true
    }
}
