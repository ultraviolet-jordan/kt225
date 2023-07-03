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

    var sceneCoordinates = Coordinates.NONE
        protected set

    var mapSquareChanged = true
        protected set
    
    var running = true
        protected set

    abstract fun login()
    abstract fun rebuildScene()
    abstract fun processRoute()
    abstract fun moveTo(coordinates: Coordinates, stepDirection: RouteStepDirection)
    abstract fun canTravel(coordinates: Coordinates, direction: EntityDirection): Boolean
    
    fun cycle() {
        processRoute()
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
        if (sceneCoordinates == coordinates) {
            return false
        }
        val lastZoneX = sceneCoordinates.zoneX
        val lastZoneZ = sceneCoordinates.zoneZ
        val zoneX = coordinates.zoneX
        val zoneZ = coordinates.zoneZ
        val limit = ((buildArea shr 3) / 2) - 1
        return abs(lastZoneX - zoneX) >= limit || abs(lastZoneZ - zoneZ) >= limit
    }
}
