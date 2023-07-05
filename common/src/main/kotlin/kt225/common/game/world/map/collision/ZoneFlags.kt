package kt225.common.game.world.map.collision

import kt225.common.game.world.Coordinates
import org.rsmod.pathfinder.ZoneCoords
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
fun ZoneFlags.change(add: Boolean, coordinates: Coordinates, mask: Int) {
    if (add) {
        add(coordinates, mask)
    } else {
        remove(coordinates, mask)
    }
}

fun ZoneFlags.add(coordinates: Coordinates, mask: Int) {
    add(coordinates.x, coordinates.z, coordinates.plane, mask)
}

fun ZoneFlags.remove(coordinates: Coordinates, mask: Int) {
    remove(coordinates.x, coordinates.z, coordinates.plane, mask)
}

operator fun ZoneFlags.set(coordinates: Coordinates, mask: Int) {
    set(coordinates.x, coordinates.z, coordinates.plane, mask)
}

fun ZoneFlags.alloc(coordinates: Coordinates) {
    alloc(ZoneCoords(coordinates.zoneX, coordinates.zoneZ, coordinates.plane))
}
