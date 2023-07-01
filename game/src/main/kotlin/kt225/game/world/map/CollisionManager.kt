package kt225.game.world.map

import kt225.cache.map.MapSquareLands
import kt225.cache.map.MapSquareLocs
import kt225.cache225.map.MapSquareLandEntryType
import kt225.cache225.map.MapSquareLocEntryType
import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareCoordinates
import kt225.common.game.world.map.MapSquareLand
import kt225.common.game.world.map.MapSquareLoc
import org.rsmod.pathfinder.ZoneFlags
import org.rsmod.pathfinder.flag.CollisionFlag

/**
 * @author Jordan Abraham
 */
class CollisionManager(
    private val zoneFlags: ZoneFlags
) {
    fun applyCollision(lands: MapSquareLands<MapSquareLandEntryType>, locs: MapSquareLocs<MapSquareLocEntryType>) {
        val area = MapSquare.AREA
        for (entry in lands) {
            val mapSquare = MapSquare(entry.key)
            for (index in 0 until 4 * area) {
                val plane = index / area
                val remaining = index % area
                val x = remaining / 64
                val z = remaining % 64
                val mapSquareCoordinates = MapSquareCoordinates(x, z, plane)
                val mapSquareLand = MapSquareLand(entry.value.lands[mapSquareCoordinates.packed.toInt()])
                if (mapSquareLand.collision and 0x1 != 1) {
                    continue
                }
                val adjustedPlane = MapSquareCoordinates(x, z, 1)
                val adjustedLand = MapSquareLand(entry.value.lands[adjustedPlane.packed.toInt()])
                val actualPlane = if (adjustedLand.collision and 0x2 == 2) plane - 1 else plane
                if (actualPlane < 0) {
                    continue
                }
                val coordinates = Coordinates(x + mapSquare.mapSquareX, z + mapSquare.mapSquareZ, plane)
                addFloorCollision(coordinates)
            }
        }
        
        for (entry in locs) {
            val mapSquare = MapSquare(entry.key)
            for (packed in entry.value.locs) {
                val loc = MapSquareLoc(packed)
                val mapSquareCoordinates = loc.coords
                val adjustedPlane = MapSquareCoordinates(mapSquareCoordinates.x, mapSquareCoordinates.z, 1)
                val adjustedLand = lands[mapSquare.packed]?.lands?.get(adjustedPlane.packed.toInt())?.let(::MapSquareLand) ?: continue
                val actualPlane = if (adjustedLand.collision and 0x2 == 2) mapSquareCoordinates.plane - 1 else mapSquareCoordinates.plane
                if (actualPlane < 0) {
                    continue
                }
                val coordinates = Coordinates(mapSquareCoordinates.x + mapSquare.mapSquareX, mapSquareCoordinates.z + mapSquare.mapSquareZ, mapSquareCoordinates.plane)
                addLocCollision(coordinates, loc)
            }
        }
    }
    
    private fun addFloorCollision(coordinates: Coordinates) {
        addCollisionFlag(coordinates, CollisionFlag.FLOOR, true)
    }
    
    private fun addLocCollision(coordinates: Coordinates, loc: MapSquareLoc) {
        changeNormalCollision(coordinates, loc, true)
    }
    
    private fun changeNormalCollision(coordinates: Coordinates, loc: MapSquareLoc, add: Boolean) {
        val shape = loc.shape
        val rotation = loc.rotation
    }

    private fun addCollisionFlag(coordinates: Coordinates, mask: Int, add: Boolean) {
        if (add) {
            zoneFlags.add(coordinates.x, coordinates.z, coordinates.plane, mask)
        } else {
            zoneFlags.remove(coordinates.x, coordinates.z, coordinates.plane, mask)
        }
    }
}
