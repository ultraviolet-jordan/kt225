package kt225.game.world.map

import kt225.cache.config.loc.Locs
import kt225.cache.map.MapSquareLands
import kt225.cache.map.MapSquareLocs
import kt225.cache225.config.loc.LocEntryType
import kt225.cache225.map.MapSquareLandEntryType
import kt225.cache225.map.MapSquareLocEntryType
import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareCoordinates
import kt225.common.game.world.map.MapSquareLand
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocShape
import kt225.game.world.map.collision.Collider
import kt225.game.world.map.collision.FloorCollider
import kt225.game.world.map.collision.LocCollider
import kt225.game.world.map.collision.WallCollider
import kt225.game.world.map.collision.WallProjectileCollider
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
class CollisionManager(
    private val zoneFlags: ZoneFlags,
    private val locs: Locs<LocEntryType>
) {
    private val collider = Collider(zoneFlags)
    private val wallCollider = WallCollider(collider)
    private val wallProjectileCollider = WallProjectileCollider(collider)
    private val floorCollider = FloorCollider(collider)
    private val locCollider = LocCollider(collider)
    
    fun applyCollision(lands: MapSquareLands<MapSquareLandEntryType>, locs: MapSquareLocs<MapSquareLocEntryType>) {
        val area = MapSquare.AREA
        for (entry in lands) {
            val mapSquare = MapSquare(entry.value.mapSquare)
            for (index in 0 until 4 * area) {
                val plane = index / area
                val remaining = index % area
                val x = remaining / 64
                val z = remaining % 64
                val mapSquareCoordinates = MapSquareCoordinates(x, z, plane)
                val mapSquareLand = MapSquareLand(entry.value.lands[mapSquareCoordinates.packed])
                if (mapSquareLand.collision and 0x1 != 1) {
                    continue
                }
                val adjustedPlane = MapSquareCoordinates(x, z, 1)
                val adjustedLand = MapSquareLand(entry.value.lands[adjustedPlane.packed])
                val actualPlane = if (adjustedLand.collision and 0x2 == 2) plane - 1 else plane
                if (actualPlane < 0) {
                    continue
                }
                val coordinates = Coordinates(x + mapSquare.mapSquareX, z + mapSquare.mapSquareZ, plane)
                changeLandCollision(coordinates, true)
            }
        }
        
        for (entry in locs) {
            val mapSquare = MapSquare(entry.value.mapSquare)
            for (packed in entry.value.locs) {
                val loc = MapSquareLoc(packed)
                val mapSquareCoordinates = loc.coords
                val adjustedPlane = MapSquareCoordinates(mapSquareCoordinates.x, mapSquareCoordinates.z, 1)
                val adjustedLand = lands[mapSquare.id]?.lands?.get(adjustedPlane.packed)?.let(::MapSquareLand) ?: continue
                val actualPlane = if (adjustedLand.collision and 0x2 == 2) mapSquareCoordinates.plane - 1 else mapSquareCoordinates.plane
                if (actualPlane < 0) {
                    continue
                }
                val coordinates = Coordinates(mapSquareCoordinates.x + mapSquare.mapSquareX, mapSquareCoordinates.z + mapSquare.mapSquareZ, mapSquareCoordinates.plane)
                changeLocCollision(loc, coordinates, true)
            }
        }
    }

    fun collisionFlag(location: Coordinates): Int {
        return zoneFlags[location.x, location.z, location.plane]
    }
    
    private fun changeLandCollision(coordinates: Coordinates, add: Boolean) {
        floorCollider.change(coordinates, add)
    }

    private fun changeLocCollision(obj: MapSquareLoc, coordinates: Coordinates, add: Boolean) {
        val entry = locs[obj.id] ?: return
        val blockrange = entry.blockrange
        val blockwalk = entry.blockwalk
        val shape = obj.shape
        val rotation = obj.rotation
        
        var interactable = entry.interactable
        if (entry.interactive == -1) {
            interactable = false
            if (((entry.shapes?.size ?: 0) > 0) && ((entry.shapes?.get(0) ?: -1) == MapSquareLocShape.CENTREPIECE_STRAIGHT.id)) {
                interactable = true
            }
            if (entry.ops != null) {
                interactable = true
            }
        }

        when {
            shape.isWall && blockwalk -> {
                wallCollider.change(coordinates, rotation, shape, add)
                if (blockrange) {
                    wallProjectileCollider.change(coordinates, rotation, shape, add)
                }
            }
            shape.isGround && blockwalk -> {
                var width = entry.width
                var length = entry.length
                if (rotation.id == 1 || rotation.id == 3) {
                    width = entry.length
                    length = entry.width
                }
                locCollider.change(coordinates, width, length, blockrange, add)
            }
            shape.isGroundDecor && blockwalk && interactable -> {
                floorCollider.change(coordinates, add)
            }
        }
    }
}
