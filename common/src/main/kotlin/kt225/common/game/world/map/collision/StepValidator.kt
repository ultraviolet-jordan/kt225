package kt225.common.game.world.map.collision

import kt225.common.game.entity.EntityDirection
import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.StepValidator
import org.rsmod.pathfinder.flag.CollisionFlag.BLOCK_NPCS

/**
 * @author Jordan Abraham
 */
fun StepValidator.canTravel(coordinates: Coordinates, direction: EntityDirection, isNPC: Boolean): Boolean {
    return canTravel(coordinates.plane, coordinates.x, coordinates.z, direction.deltaX, direction.deltaZ, 1, if (isNPC) BLOCK_NPCS else 0)
}