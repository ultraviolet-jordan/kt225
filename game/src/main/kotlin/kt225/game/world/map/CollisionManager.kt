package kt225.game.world.map

import kt225.cache.config.loc.Locs
import kt225.cache.map.MapSquareLands
import kt225.cache.map.MapSquareLocs
import kt225.cache225.config.loc.LocEntryType
import kt225.cache225.map.MapSquareLandEntryType
import kt225.cache225.map.MapSquareLocEntryType
import kt225.common.game.entity.EntityDirection
import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareCoordinates
import kt225.common.game.world.map.MapSquareLand
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocLayer.Companion.GROUND
import kt225.common.game.world.map.MapSquareLocLayer.Companion.GROUND_DECOR
import kt225.common.game.world.map.MapSquareLocLayer.Companion.WALL
import kt225.common.game.world.map.MapSquareLocRotation.Companion.NORTH
import kt225.common.game.world.map.MapSquareLocRotation.Companion.SOUTH
import kt225.common.game.world.map.collision.*
import org.rsmod.pathfinder.StepValidator
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
class CollisionManager(
    mapSquareLands: MapSquareLands<MapSquareLandEntryType>,
    mapSquareLocs: MapSquareLocs<MapSquareLocEntryType>,
    zoneFlags: ZoneFlags,
    private val stepValidator: StepValidator,
    private val locs: Locs<LocEntryType>
) {
    private val floorCollider = FloorCollider(zoneFlags)
    private val wallCollider = WallCollider(zoneFlags)
    private val locCollider = LocCollider(zoneFlags)
    private val openCollider = OpenCollider(zoneFlags)
    
    init {
        val area = MapSquare.AREA

        for (entry in mapSquareLands.values) {
            val mapSquare = MapSquare(entry.mapSquare)
            val mapSquareX = mapSquare.mapSquareX
            val mapSquareZ = mapSquare.mapSquareZ
            for (index in 0 until 4 * area) {
                val plane = index / area
                val remaining = index % area
                val x = remaining / 64
                val z = remaining % 64
                val absoluteX = x + mapSquareX
                val absoluteZ = z + mapSquareZ
                // We must initialize the entire mapsquare with flag 0x0 because of how the pathfinder works.
                // There is a possibility of an entire zone not being initialized with zero clipping
                // depending on if that zone contains anything to clip from the cache or not.
                // So this way guarantees every zone in our mapsquares are properly initialized for the pathfinder.
                openCollision(Coordinates(absoluteX, absoluteZ, plane))

                val mapSquareLandEntryType = mapSquareLands[mapSquare.id] ?: continue
                val originalCoords = MapSquareCoordinates(x, z, plane)
                val mapSquareLand = MapSquareLand(mapSquareLandEntryType.lands[originalCoords.packed])
                if (mapSquareLand.collision and 0x1 != 1) {
                    continue
                }
                val adjustedCoords = MapSquareCoordinates(x, z, 1)
                val adjustedLand = MapSquareLand(mapSquareLandEntryType.lands[adjustedCoords.packed])
                val adjustedPlane = if (adjustedLand.collision and 0x2 == 2) plane - 1 else plane
                if (adjustedPlane < 0) {
                    continue
                }
                changeLandCollision(
                    coordinates = Coordinates(absoluteX, absoluteZ, adjustedPlane),
                    add = true
                )
            }
        }

        for (entry in mapSquareLocs.values) {
            val mapSquare = MapSquare(entry.mapSquare)
            val mapSquareX = mapSquare.mapSquareX
            val mapSquareZ = mapSquare.mapSquareZ
            for (packed in entry.locs) {
                val loc = MapSquareLoc(packed)
                val originalCoords = loc.coords
                val plane = originalCoords.plane
                val x = originalCoords.x
                val z = originalCoords.z
                val adjustedCoords = MapSquareCoordinates(x, z, 1)
                val adjustedLand = mapSquareLands[mapSquare.id]?.lands?.get(adjustedCoords.packed)?.let(::MapSquareLand) ?: continue
                val adjustedPlane = if (adjustedLand.collision and 0x2 == 2) plane - 1 else plane
                if (adjustedPlane < 0) {
                    continue
                }
                changeLocCollision(
                    loc = loc,
                    coordinates = Coordinates(x + mapSquareX, z + mapSquareZ, adjustedPlane),
                    add = true
                )
            }
        }
    }

    fun canTravel(coordinates: Coordinates, direction: EntityDirection, isNPC: Boolean): Boolean {
        return stepValidator.canTravel(coordinates, direction, isNPC)
    }

    private fun openCollision(coordinates: Coordinates) {
        openCollider.open(coordinates)
    }
    
    private fun changeLandCollision(coordinates: Coordinates, add: Boolean) {
        floorCollider.change(coordinates, add)
    }

    private fun changeLocCollision(loc: MapSquareLoc, coordinates: Coordinates, add: Boolean) {
        val entry = locs[loc.id] ?: return
        val blockwalk = entry.blockwalk
        // Blockwalk is required to apply collision changes.
        if (!blockwalk) {
            return
        }

        val blockproj = entry.blockproj
        val shape = loc.shape
        val rotation = loc.rotation
        when (shape.layer) {
            WALL -> wallCollider.change(coordinates, rotation, shape, blockproj, add)
            GROUND -> when (rotation) {
                NORTH, SOUTH -> locCollider.change(coordinates, entry.length, entry.width, blockproj, add)
                else -> locCollider.change(coordinates, entry.width, entry.length, blockproj, add)
            }
            GROUND_DECOR -> {
                val intractable = when (entry.interactive) {
                    -1 -> entry.ops != null
                    else -> entry.intractable
                }
                if (intractable) {
                    floorCollider.change(coordinates, add)
                }
            }
        }
    }
}
