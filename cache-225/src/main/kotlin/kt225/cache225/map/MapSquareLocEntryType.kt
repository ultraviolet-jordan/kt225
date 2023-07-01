package kt225.cache225.map

import kt225.cache.EntryType
import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareCoordinates
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocLayer

/**
 * @author Jordan Abraham
 */
data class MapSquareLocEntryType(
    val mapSquare: Int,
    val locs: MutableList<Long> = ArrayList()
) : EntryType {
    fun removeLoc(loc: MapSquareLoc): Boolean {
        return locs.remove(loc.packed)
    }

    fun addLoc(loc: MapSquareLoc): Boolean {
        if (locs.contains(loc.packed)) {
            return false
        }
        val packed = loc.packed
        locs.add(packed)
        return locs.contains(packed)
    }

    fun query(
        coordinates: Coordinates, 
        layer: MapSquareLocLayer, 
        function: (loc: MapSquareLoc?) -> Unit
    ) {
        val loc = queryLoc(coordinates, layer)
        function.invoke(loc)
    }

    fun queryWithCoordinates(
        coordinates: Coordinates, 
        layer: MapSquareLocLayer, 
        function: (loc: MapSquareLoc?, coord: MapSquareCoordinates) -> Unit
    ) {
        val loc = queryLoc(coordinates, layer)
        val mapSquareCoordinates = MapSquareCoordinates(
            x = coordinates.localX,
            z = coordinates.localZ,
            plane = coordinates.plane
        )
        function.invoke(loc, mapSquareCoordinates)
    }
    
    private fun queryLoc(coordinates: Coordinates, layer: MapSquareLocLayer): MapSquareLoc? {
        require(coordinates.mapSquare == MapSquare(mapSquare))
        val mapSquareCoordinates = MapSquareCoordinates(
            x = coordinates.localX,
            z = coordinates.localZ,
            plane = coordinates.plane
        )
        return locs.firstOrNull {
            val loc = MapSquareLoc(it)
            loc.coords == mapSquareCoordinates && loc.shape.layer.id == layer.id
        }?.let(::MapSquareLoc)
    }
}
