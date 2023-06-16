package kt225.game.world.map

import kt225.cache.map.MapSquares
import kt225.cache225.map.MapSquareEntryType
import kt225.cache225.map.MapSquareLand
import kt225.cache225.map.MapSquareLoc
import kt225.cache225.map.MapSquareLocalPosition

/**
 * @author Jordan Abraham
 */
class CollisionManager {
    fun applyCollision(maps: MapSquares<MapSquareEntryType>) {
        for (map in maps) {
            for (plane in 0 until 4) {
                for (x in 0 until 64) {
                    for (z in 0 until 64) {
                        addLandCollision(map, plane, x, z)
                    }
                }
            }
            addLocCollision(map)
        }
    }

    private fun addLandCollision(entry: MutableMap.MutableEntry<Int, MapSquareEntryType>, plane: Int, x: Int, z: Int) {
        val land = MapSquareLand(entry.value.lands[entry.key])
        if (land.collision and 0x1 != 0x1) {
            return
        }
        val positionAtPlaneOne = MapSquareLocalPosition(1, x, z)
        val landAtPlaneOne = MapSquareLand(entry.value.lands[positionAtPlaneOne.packed])
        val adjustedPlane = if (landAtPlaneOne.collision and 0x2 == 0x2) plane - 1 else plane
        if (adjustedPlane < 0) {
            return
        }
        val mapSquare = entry.value.mapSquare
        val baseX = mapSquare.x shl 6
        val baseZ = mapSquare.z shl 6
        // TODO Add land collision.
    }

    private fun addLocCollision(entry: MutableMap.MutableEntry<Int, MapSquareEntryType>) {
        val mapSquare = entry.value.mapSquare
        val baseX = mapSquare.x shl 6
        val baseZ = mapSquare.z shl 6
        val locs = entry.value.locs[entry.key] ?: return
        for (packed in locs) {
            val loc = MapSquareLoc(packed)
            // TODO Add loc collision.
        }
    }
}
