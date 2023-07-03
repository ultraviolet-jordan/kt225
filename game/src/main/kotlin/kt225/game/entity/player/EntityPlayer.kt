package kt225.game.entity.player

import kt225.common.game.Client
import kt225.common.game.entity.EntityDirection
import kt225.common.game.entity.animator.type.Teleport
import kt225.common.game.entity.player.Player
import kt225.common.game.entity.render.type.Appearance
import kt225.common.game.world.Coordinates
import kt225.game.world.GameWorld
import kt225.packet.type.server.LoadAreaPacket

/**
 * @author Jordan Abraham
 */
class EntityPlayer(
    username: String,
    client: Client,
    override val world: GameWorld
) : Player(
    username = username,
    client = client,
    world = world,
    renderer = PlayerRenderer(),
    animator = PlayerAnimator()
) {
    override fun init(coordinates: Coordinates) {
        super.init(coordinates)
        client.flushWriteQueue() // Flushes the login response.
    }

    override fun login() {
        client.writePacket(LoadAreaPacket(coordinates.zoneX, coordinates.zoneZ))
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
                x = coordinates.x - coordinates.zoneOriginX,
                z = coordinates.z - coordinates.zoneOriginZ,
                plane = coordinates.plane
            )
        )
        online = true
    }

    override fun processRoute() {
        route.process(this)
    }

    override fun canTravel(coordinates: Coordinates, direction: EntityDirection): Boolean {
        return world.collisionManager.canTravel(coordinates, direction, false)
    }
}
