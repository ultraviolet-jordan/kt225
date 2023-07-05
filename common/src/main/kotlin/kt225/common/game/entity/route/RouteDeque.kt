package kt225.common.game.entity.route

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.RouteCoordinates
import java.util.*
import kotlin.math.sign

/**
 * @author Jordan Abraham
 */
class RouteDeque(
    private val steps: Deque<Coordinates> = LinkedList()
) : Deque<Coordinates> by steps {
    private val checkpoints: Deque<RouteCoordinates> = LinkedList()

    fun calculateNextSteps(location: Coordinates) {
        if (isNotEmpty() || checkpoints.isEmpty()) {
            return
        }
        clear()
        val polled = checkpoints.poll()
        val currentX = location.x
        val currentZ = location.z
        val waypointX = polled.x
        val waypointZ = polled.y
        queueStep(currentX, currentZ, waypointX, waypointZ, (waypointX - currentX).sign, (waypointZ - currentZ).sign, location.plane, 0)
    }

    private tailrec fun queueStep(
        currentX: Int, 
        currentZ: Int, 
        waypointX: Int, 
        waypointZ: Int, 
        xSign: Int, 
        zSign: Int, 
        plane: Int,
        count: Int
    ) {
        if ((currentX == waypointX && currentZ == waypointZ) || count == 25) {
            return
        }
        steps.add(Coordinates(currentX + xSign, currentZ + zSign, plane))
        return queueStep(currentX + xSign, currentZ + zSign, waypointX, waypointZ, xSign, zSign, plane, count + 1)
    }

    fun appendRoute(route: Route) {
        clear()
        checkpoints.clear()
        checkpoints.addAll(route)
    }
}
