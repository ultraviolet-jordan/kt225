package kt225.game.world.map

import kt225.cache.map.MapSquareLands
import kt225.cache.map.MapSquareLocs
import kt225.cache225.map.MapSquareLandEntryType
import kt225.cache225.map.MapSquareLocEntryType
import kt225.common.game.world.Position
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareLand
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocResource
import kt225.common.game.world.map.MapSquarePosition
import org.rsmod.pathfinder.ZoneFlags
import org.rsmod.pathfinder.flag.CollisionFlag

/**
 * @author Jordan Abraham
 */
class CollisionManager(
    private val zoneFlags: ZoneFlags
) {
    fun applyCollision(lands: MapSquareLands<MapSquareLandEntryType>, locs: MapSquareLocs<MapSquareLocEntryType>) {
        for (entry in lands) {
            val mapSquare = MapSquare(entry.key)
            for (plane in 0 until 4) {
                for (x in 0 until 64) {
                    for (z in 0 until 64) {
                        val mapSquarePosition = MapSquarePosition(x, z, plane)
                        val mapSquareLand = MapSquareLand(entry.value.lands[mapSquarePosition.packed])
                        if (mapSquareLand.collision and 0x1 != 1) {
                            continue
                        }
                        val adjustedPlane = MapSquarePosition(x, z, 1)
                        val adjustedLand = MapSquareLand(entry.value.lands[adjustedPlane.packed])
                        val actualPlane = if (adjustedLand.collision and 0x2 == 2) plane - 1 else plane
                        if (actualPlane < 0) {
                            continue
                        }
                        val position = Position(x + mapSquare.mapSquareX, z + mapSquare.mapSquareZ, plane)
                        addFloorCollision(position)
                    }
                }
            }
        }
        
        for (entry in locs) {
            val mapSquare = MapSquare(entry.key)
            for (packed in entry.value.locs) {
                val resource = MapSquareLocResource(packed)
                val mapSquarePosition = MapSquarePosition(resource.position)
                val adjustedPlane = MapSquarePosition(mapSquarePosition.x, mapSquarePosition.z, 1)
                val adjustedLand = lands[mapSquare.packed]?.lands?.get(adjustedPlane.packed)?.let(::MapSquareLand) ?: continue
                val actualPlane = if (adjustedLand.collision and 0x2 == 2) mapSquarePosition.plane - 1 else mapSquarePosition.plane
                if (actualPlane < 0) {
                    continue
                }
                val loc = MapSquareLoc(resource.loc)
                val position = Position(mapSquarePosition.x + mapSquare.mapSquareX, mapSquarePosition.z + mapSquare.mapSquareZ, mapSquarePosition.plane)
                addLocCollision(position, loc)
            }
        }
    }
    
    private fun addFloorCollision(position: Position) {
        addCollisionFlag(position, CollisionFlag.FLOOR, true)
    }
    
    private fun addLocCollision(position: Position, loc: MapSquareLoc) {
        changeNormalCollision(position, loc, true)
    }
    
    private fun changeNormalCollision(position: Position, loc: MapSquareLoc, add: Boolean) {
        val shape = loc.shape
        val rotation = loc.rotation
    }

    private fun addCollisionFlag(position: Position, mask: Int, add: Boolean) {
        if (add) {
            zoneFlags.add(position.x, position.z, position.plane, mask)
        } else {
            zoneFlags.remove(position.x, position.z, position.plane, mask)
        }
    }
}
