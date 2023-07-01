package kt225.cache225.map

import kt225.cache.EntryType
import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareCoordinates
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocLayer
import kt225.common.game.world.map.MapSquareLocResource

/**
 * @author Jordan Abraham
 */
data class MapSquareLocEntryType(
    val mapSquare: Int,
    val locs: MutableList<Long> = ArrayList()
) : EntryType {
    fun getLoc(coordinates: Coordinates, layer: MapSquareLocLayer, function: (loc: MapSquareLoc?, coords: MapSquareCoordinates) -> Unit) {
        require(coordinates.mapSquare == MapSquare(mapSquare))
        val mapSquareCoordinates = MapSquareCoordinates(
            x = coordinates.localX,
            z = coordinates.localZ,
            plane = coordinates.plane,
            layer = layer.id
        )
        val resource = locs.firstOrNull { MapSquareLocResource(it).coords == mapSquareCoordinates.packed }?.let(::MapSquareLocResource)
        val loc = resource?.let { MapSquareLoc(it.loc) }
        function.invoke(loc, mapSquareCoordinates)
    }
    
    fun removeLoc(loc: MapSquareLoc, coordinates: MapSquareCoordinates): Boolean {
        val resource = MapSquareLocResource(coordinates.packed, loc.packed)
        return locs.remove(resource.packed)
    }

    fun addLoc(loc: MapSquareLoc, coordinates: MapSquareCoordinates): Boolean {
        val resource = MapSquareLocResource(coordinates.packed, loc.packed)
        if (locs.contains(resource.packed)) {
            return false
        }
        val packed = resource.packed
        locs.add(packed)
        return locs.contains(packed)
    }
}
