package kt225.packet.handler

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.common.game.Client
import kt225.common.packet.PacketHandler
import kt225.packet.type.client.MoveMiniMapPacket
import org.rsmod.pathfinder.PathFinder

/**
 * @author Jordan Abraham
 */
@Singleton
class MoveMiniMapPacketHandler @Inject constructor(
    private val pathfinder: PathFinder
) : PacketHandler<MoveMiniMapPacket>(
    groupId = 1
) {
    override fun handlePacket(packet: MoveMiniMapPacket, client: Client) {
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
