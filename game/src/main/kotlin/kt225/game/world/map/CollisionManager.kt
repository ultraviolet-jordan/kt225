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
import kt225.common.game.world.map.collision.FloorCollider
import kt225.common.game.world.map.collision.LocCollider
import kt225.common.game.world.map.collision.RoofCollider
import kt225.common.game.world.map.collision.WallCollider
import kt225.common.game.world.map.collision.alloc
import kt225.common.game.world.map.collision.canTravel
import org.rsmod.pathfinder.StepValidator
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
class CollisionManager(
    private val mapSquareLands: MapSquareLands<MapSquareLandEntryType>,
    private val mapSquareLocs: MapSquareLocs<MapSquareLocEntryType>,
    private val zoneFlags: ZoneFlags,
    private val stepValidator: StepValidator,
    private val locs: Locs<LocEntryType>
) {
    private val floorCollider = FloorCollider(zoneFlags)
    private val wallCollider = WallCollider(zoneFlags)
    private val locCollider = LocCollider(zoneFlags)
    private val roofCollider = RoofCollider(zoneFlags)
    
    init {
        initCollision()
    }

    private fun initCollision() {
        val area = MapSquare.AREA
        for (entry in mapSquareLands.values) {
            val mapSquare = MapSquare(entry.mapSquare)
            val lands = mapSquareLands[mapSquare.id] ?: continue
            val locs = mapSquareLocs[mapSquare.id] ?: continue
            val mapSquareX = mapSquare.mapSquareX
            val mapSquareZ = mapSquare.mapSquareZ
            for (index in 0 until 4 * area) {
                val plane = index / area
                val remaining = index % area
                val x = remaining / 64
                val z = remaining % 64
                val absoluteX = x + mapSquareX
                val absoluteZ = z + mapSquareZ

                // There is a possibility of an entire zone not being initialized with zero clipping
                // depending on if that zone contains anything to clip from the cache or not.
                // So this way guarantees every zone in our mapsquares are properly initialized for the pathfinder.
                zoneFlags.alloc(Coordinates(absoluteX, absoluteZ, plane))

                val coords = MapSquareCoordinates(x, z, plane)
                val mapSquareLand = MapSquareLand(lands.lands[coords.packed])

                if (mapSquareLand.collision and 0x4 != 0) {
                    changeRoofCollision(Coordinates(absoluteX, absoluteZ, plane), true)
                }

                if (mapSquareLand.collision and 0x1 != 1) {
                    continue
                }

                val adjustedLand = MapSquareLand(lands.lands[MapSquareCoordinates(x, z, 1).packed])
                val adjustedPlane = if (adjustedLand.collision and 0x2 == 2) plane - 1 else plane
                if (adjustedPlane < 0) {
                    continue
                }
                changeLandCollision(Coordinates(absoluteX, absoluteZ, adjustedPlane), true)
            }
            for (packed in locs.locs) {
                val mapSquareLoc = MapSquareLoc(packed)
                val coords = mapSquareLoc.coords
                val x = coords.x
                val z = coords.z
                val plane = coords.plane
                val adjustedLand = MapSquareLand(lands.lands[MapSquareCoordinates(x, z, 1).packed])
                val adjustedPlane = if (adjustedLand.collision and 0x2 == 2) plane - 1 else plane
                if (adjustedPlane < 0) {
                    continue
                }
                changeLocCollision(mapSquareLoc, Coordinates(x + mapSquareX, z + mapSquareZ, adjustedPlane), true)
            }
        }
    }

    fun canTravel(coordinates: Coordinates, direction: EntityDirection, isNPC: Boolean): Boolean {
        return stepValidator.canTravel(coordinates, direction, isNPC)
    }
    
    private fun changeLandCollision(coordinates: Coordinates, add: Boolean) {
        floorCollider.change(coordinates, add)
    }

    private fun changeLocCollision(loc: MapSquareLoc, coordinates: Coordinates, add: Boolean) {
        val entry = locs[loc.id] ?: return

        // Blockwalk is required to apply collision changes.
        if (!entry.blockwalk) {
            return
        }

        val shape = loc.shape
        val rotation = loc.rotation
        when (shape.layer) {
            WALL -> wallCollider.change(coordinates, rotation, shape, entry.blockproj, add)
            GROUND -> when (rotation) {
                NORTH, SOUTH -> locCollider.change(coordinates, entry.length, entry.width, entry.blockproj, add)
                else -> locCollider.change(coordinates, entry.width, entry.length, entry.blockproj, add)
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

    private fun changeRoofCollision(coordinates: Coordinates, add: Boolean) {
        roofCollider.change(coordinates, add);
    }
}
