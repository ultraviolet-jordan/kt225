package kt225.cache225.map

import kt225.cache.EntryType
import kt225.common.game.world.Position
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocLayer
import kt225.common.game.world.map.MapSquareLocResource
import kt225.common.game.world.map.MapSquarePosition

/**
 * @author Jordan Abraham
 */
data class MapSquareLocEntryType(
    val mapSquare: Int,
    val locs: MutableList<Long> = ArrayList()
) : EntryType {
    fun getLoc(position: Position, layer: MapSquareLocLayer, function: (loc: MapSquareLoc?, position: MapSquarePosition) -> Unit) {
        require(position.mapSquare == MapSquare(mapSquare))
        val mapSquarePosition = MapSquarePosition(
            x = position.localX,
            z = position.localZ,
            plane = position.plane,
            layer = layer.id
        )
        val resource = locs.firstOrNull { MapSquareLocResource(it).position == mapSquarePosition.packed }?.let(::MapSquareLocResource)
        val loc = resource?.let { MapSquareLoc(it.loc) }
        function.invoke(loc, mapSquarePosition)
    }
    
    fun removeLoc(loc: MapSquareLoc, position: MapSquarePosition): Boolean {
        val resource = MapSquareLocResource(position.packed, loc.packed)
        return locs.remove(resource.packed)
    }

    fun addLoc(loc: MapSquareLoc, position: MapSquarePosition): Boolean {
        val resource = MapSquareLocResource(position.packed, loc.packed)
        if (locs.contains(resource.packed)) {
            return false
        }
        val packed = resource.packed
        locs.add(packed)
        return locs.contains(packed)
    }
}
