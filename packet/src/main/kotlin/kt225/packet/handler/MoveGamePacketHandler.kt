package kt225.packet.handler

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.common.game.Client
import kt225.common.packet.PacketHandler
import kt225.packet.type.client.MoveGamePacket
import org.rsmod.pathfinder.PathFinder

/**
 * @author Jordan Abraham
 */
@Singleton
class MoveGamePacketHandler @Inject constructor(
    private val pathfinder: PathFinder
) : PacketHandler<MoveGamePacket>(
    groupId = 1
) {
    override fun handlePacket(packet: MoveGamePacket, client: Client) {
        val player = client.player ?: return
        val path = pathfinder.findPath(
            srcX = player.coordinates.x,
            srcY = player.coordinates.z,
            destX = packet.destinationX,
            destY = packet.destinationZ,
            z = player.coordinates.plane
        )
        player.route.appendRoute(path)
    }
}
