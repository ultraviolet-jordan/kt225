package kt225.common.game.entity

import kt225.common.game.entity.route.RouteDeque
import kt225.common.game.entity.route.RouteStepDirection
import kt225.common.game.world.Coordinates
import kt225.common.game.world.World
import kotlin.math.abs

/**
 * @author Jordan Abraham
 */
abstract class Entity(
    open val world: World
) {
    val route = RouteDeque()
    
    var index = 0

    var coordinates = Coordinates.NONE
        protected set

    var lastCoordinates = Coordinates.NONE
        protected set

    var mapSquareChanged = true
        protected set
    
    var running = true
        protected set

    abstract fun login()
    abstract fun rebuildScene()
    abstract fun moveTo(coordinates: Coordinates, stepDirection: RouteStepDirection)
    abstract fun canTravel(coordinates: Coordinates, direction: EntityDirection): Boolean
    
    fun cycle() {
        val coordinates = this.coordinates
        route.calculateNextSteps(coordinates)
        var polled = route.poll() ?: return
        val walkDirection = EntityDirection.between(coordinates, polled)
        if (walkDirection == EntityDirection.NONE || !canTravel(coordinates, walkDirection)) {
            return
        }
        var runDirection = EntityDirection.NONE
        var nextCoordinates = polled
        if (running) {
            route.calculateNextSteps(nextCoordinates)
            polled = route.poll() ?: return run {
                moveTo(nextCoordinates, RouteStepDirection(walkDirection, EntityDirection.NONE))
            }
            runDirection = EntityDirection.between(nextCoordinates, polled)
            if (!canTravel(nextCoordinates, runDirection)) {
                route.clear()
                runDirection = EntityDirection.NONE
            } else {
                nextCoordinates = polled
            }
        }
        moveTo(nextCoordinates, RouteStepDirection(walkDirection, runDirection))
        
        if (needSceneRebuild()) {
            rebuildScene()
        }
    }

    fun reset() {
        if (mapSquareChanged) {
            mapSquareChanged = false
        }
        if (lastCoordinates != coordinates) {
            lastCoordinates = coordinates
        }
    }

    private fun needSceneRebuild(buildArea: Int = 104): Boolean {
        val lastZoneX = coordinates.zoneX
        val lastZoneZ = coordinates.zoneZ
        val zoneX = coordinates.zoneX
        val zoneZ = coordinates.zoneZ
        val limit = ((buildArea shr 3) / 2) - 1
        return abs(lastZoneX - zoneX) >= limit || abs(lastZoneZ - zoneZ) >= limit
    }
}
