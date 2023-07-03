package kt225.common.game.entity.route

import kt225.common.game.entity.Entity
import kt225.common.game.entity.EntityDirection
import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.Route
import org.rsmod.pathfinder.RouteCoordinates
import java.util.Deque
import java.util.LinkedList
import kotlin.math.sign

/**
 * @author Jordan Abraham
 */
class RouteDeque(
    private val steps: Deque<Coordinates> = LinkedList()
) : Deque<Coordinates> by steps {
    private val routeSteps: Deque<RouteCoordinates> = LinkedList()

    fun process(entity: Entity) {
        calculateNextTravelPoint(entity.coordinates)
        var nextWalkStep = poll() ?: return
        val walkDirection = EntityDirection.between(entity.coordinates, nextWalkStep)
        var runDirection = EntityDirection.NONE
        if (walkDirection == EntityDirection.NONE || !entity.canTravel(entity.coordinates, walkDirection)) return
        var nextCoordinates = nextWalkStep
        if (entity.running) {
            calculateNextTravelPoint(nextCoordinates)
            nextWalkStep = poll() ?: return run {
                entity.moveTo(nextCoordinates, RouteStepDirection(walkDirection, EntityDirection.NONE))
            }
            runDirection = EntityDirection.between(nextCoordinates, nextWalkStep)
            if (!entity.canTravel(nextCoordinates, runDirection)) {
                clear()
                runDirection = EntityDirection.NONE
            } else {
                nextCoordinates = nextWalkStep
            }
        }
        entity.moveTo(nextCoordinates, RouteStepDirection(walkDirection, runDirection))
    }
    
    private fun calculateNextTravelPoint(coordinates: Coordinates) {
        if (isNotEmpty() || routeSteps.isEmpty()) {
            return
        }
        clear()
        val step = routeSteps.poll()
        var currentX = coordinates.x
        var currentZ = coordinates.z
        val destX = step.x
        val destY = step.y
        val xSign = (destX - currentX).sign
        val ySign = (destY - currentZ).sign
        var count = 0
        while (currentX != destX || currentZ != destY) {
            currentX += xSign
            currentZ += ySign
            add(Coordinates(currentX, currentZ, coordinates.plane))
            if (++count > 25) break
        }
    }

    fun appendRoute(route: Route) {
        routeSteps.clear()
        clear()
        routeSteps.addAll(route)
    }
}
