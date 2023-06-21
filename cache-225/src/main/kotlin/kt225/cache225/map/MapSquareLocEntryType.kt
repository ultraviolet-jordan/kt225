package kt225.cache225.map

import kt225.cache.EntryType
import kt225.common.game.world.Position

/**
 * @author Jordan Abraham
 */
data class MapSquareLocEntryType(
    val mapSquare: Int,
    val locs: MutableMap<Int, Array<Long?>> = HashMap()
) : EntryType {
    fun query(position: Position, function: (result: List<MapSquareLoc>, local: MapSquareLocalPosition) -> Unit) {
        val mapSquare = MapSquare(mapSquare)
        val baseX = mapSquare.x shl 6
        val baseZ = mapSquare.z shl 6
        val localPosition = MapSquareLocalPosition(position.plane, position.x - baseX, position.z - baseZ)
        val mapSquareLocs = locs[localPosition.packed]!!.mapNotNull { it?.let(::MapSquareLoc) }
        function.invoke(mapSquareLocs, localPosition)
    }
    
    fun removeLoc(loc: MapSquareLoc, local: MapSquareLocalPosition): Boolean {
        val locs = locs[local.packed] ?: return false
        val index = locs.indexOf(loc.packed)
        if (index == -1) {
            return false
        }
        locs[index] = null
        return true
    }

    fun addLoc(loc: MapSquareLoc, local: MapSquareLocalPosition): Boolean {
        val existing = locs[local.packed]
        if (existing != null) {
            val index = existing.indexOf(loc.packed)
            if (index == -1) { // If new loc on this pos.
                val length = existing.size
                val clone = existing.copyOf(length + 1)
                clone[length] = loc.packed
                locs[local.packed] = clone
                return true
            }
            existing[index] = loc.packed
            return true
        }
        locs[local.packed] = Array(1) { loc.packed }
        return locs[local.packed] != null
    }
}
