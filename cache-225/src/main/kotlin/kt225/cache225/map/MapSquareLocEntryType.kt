package kt225.cache225.map

import kt225.cache.EntryType
import kt225.common.game.world.Position
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocLayer
import kt225.common.game.world.map.MapSquarePosition
import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
data class MapSquareLocEntryType(
    val mapSquare: Int,
    val locs: MutableMap<Int, Int> = TreeMap()
) : EntryType {
    fun getLoc(position: Position, layer: MapSquareLocLayer, function: (loc: MapSquareLoc?, position: MapSquarePosition) -> Unit) {
        require(position.mapSquare == MapSquare(mapSquare))
        val mapSquarePosition = MapSquarePosition(
            x = position.localX,
            z = position.localZ,
            plane = position.plane,
            layer = layer.id
        )
        val loc = locs[mapSquarePosition.packed]?.let(::MapSquareLoc)
        function.invoke(loc, mapSquarePosition)
    }
    
    fun removeLoc(loc: MapSquareLoc, position: MapSquarePosition): Boolean {
        return locs.remove(position.packed, loc.packed)
    }

    fun addLoc(loc: MapSquareLoc, position: MapSquarePosition): Boolean {
        val packed = position.packed
        locs[packed] = loc.packed
        return locs[packed] == loc.packed
    }
}
