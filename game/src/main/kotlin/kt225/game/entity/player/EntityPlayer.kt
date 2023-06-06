package kt225.game.entity.player

import kt225.common.game.Client
import kt225.common.game.entity.animator.Animator
import kt225.common.game.entity.player.Player
import kt225.common.game.entity.render.Renderer
import kt225.common.game.world.Position
import kt225.common.game.world.World
import kt225.packet225.type.server.LoadAreaPacket

/**
 * @author Jordan Abraham
 */
class EntityPlayer(
    client: Client,
    world: World
) : Player(client, world) {
    private val renderer = PlayerRenderer()
    private val animator = PlayerAnimator()

    override fun init(position: Position) {
        super.init(position)
        client.flushWriteChannel()
    }

    override fun login() {
        client.writePacket(LoadAreaPacket(position.zoneX, position.zoneZ))
    }

    override fun renderer(): Renderer {
        return renderer
    }

    override fun animator(): Animator {
        return animator
    }
}
