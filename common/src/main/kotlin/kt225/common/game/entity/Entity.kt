package kt225.common.game.entity

import kt225.common.game.entity.route.RouteDeque
import kt225.common.game.entity.route.RouteStepDirection
import kt225.common.game.world.Coordinates
import kt225.common.game.world.World

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
    abstract fun processRoute()
    abstract fun moveTo(coordinates: Coordinates, stepDirection: RouteStepDirection)
    abstract fun canTravel(coordinates: Coordinates, direction: EntityDirection): Boolean
    
    fun cycle() {
        processRoute()
    }

    fun reset() {
        if (mapSquareChanged) {
            mapSquareChanged = false
        }
        if (lastCoordinates != coordinates) {
            lastCoordinates = coordinates
        }
    }
}
