package kt225.game.entity.player

import kt225.common.game.Client
import kt225.common.game.entity.animator.type.Teleport
import kt225.common.game.entity.player.Player
import kt225.common.game.entity.render.type.Appearance
import kt225.common.game.world.Position
import kt225.common.game.world.World
import kt225.packet.type.server.LoadAreaPacket

/**
 * @author Jordan Abraham
 */
class EntityPlayer(
    username: String,
    client: Client,
    world: World
) : Player(
    username = username,
    client = client,
    world = world,
    renderer = PlayerRenderer(),
    animator = PlayerAnimator()
) {
    override fun init(position: Position) {
        super.init(position)
        client.flushWriteQueue() // Flushes the login response.
    }

    override fun login() {
        client.writePacket(LoadAreaPacket(position.zoneX, position.zoneZ))
        renderer.render(
            Appearance(
                headIcon = 0,
                name = username,
                combatLevel = 44
            )
        )
        animator.animate(
            Teleport(
                rendering = true,
                x = position.x - position.zoneOriginX,
                z = position.z - position.zoneOriginZ,
                plane = position.plane
            )
        )
        online = true
    }
}
