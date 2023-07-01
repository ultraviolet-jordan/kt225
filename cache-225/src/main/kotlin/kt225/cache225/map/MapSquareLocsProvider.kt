@file:Suppress("DuplicatedCode")

package kt225.cache225.map

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.map.MapLocs
import kt225.cache.map.MapResource
import kt225.cache.map.MapSquareLocs
import kt225.common.buffer.g1
import kt225.common.buffer.gsmarts
import kt225.common.buffer.p1
import kt225.common.buffer.psmarts
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareCoordinates
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocResource
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocShape
import java.nio.ByteBuffer
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
@Singleton
class MapSquareLocsProvider @Inject constructor(
    private val maps: MapLocs
) : MapSquaresProvider<MapSquareLocEntryType, MapSquareLocs<MapSquareLocEntryType>> {
    override fun get(): MapSquareLocs<MapSquareLocEntryType> {
        val locs = MapSquareLocs<MapSquareLocEntryType>()
        
        maps.filter { it.name.startsWith("l") }.parallelStream().forEach {
            val mapSquare = MapSquare(it.id, it.x, it.z)
            val entry = MapSquareLocEntryType(mapSquare.packed)
            decode(ByteBuffer.wrap(it.bytes).decompress(), entry)
            locs[it.id] = entry
        }
        
        return locs
    }

    override fun write(entries: MapSquareLocs<MapSquareLocEntryType>) {
        entries.values.forEach { entry ->
            val mapSquare = MapSquare(entry.mapSquare)
            val id = mapSquare.id
            val x = mapSquare.x
            val z = mapSquare.z
            val name = "l${x}_$z"

            val buffer = ByteBuffer.allocate(100_000)
            encode(buffer, entry)
            buffer.flip()

            val compressed = buffer.compress()
            val crc = CRC32().also { it.update(compressed) }.value.toInt()
            val existing = maps.firstOrNull { it.id == id && it.name == name }
            val index = maps.indexOf(existing)
            if (index != -1) {
                maps[index] = MapResource(name, id, x, z, compressed, crc)
            }
        }
    }

    override fun decode(buffer: ByteBuffer, entry: MapSquareLocEntryType): MapSquareLocEntryType {
        return buffer.decodeMapSquareLocs(entry)
    }

    override fun encode(buffer: ByteBuffer, entry: MapSquareLocEntryType) {
        // We have to group the locs together in a specific way for it to encode properly.
        // Locs have to be grouped together by their packed position.
        // Then sorted in order of the packed position.
        // Then sorted in order by their locId.
        val sortedLocs = entry
            .locs
            .map { 
                val resource = MapSquareLocResource(it)
                val adjusted = MapSquareCoordinates(resource.coords)
                val actual = MapSquareCoordinates(
                    x = adjusted.x,
                    z = adjusted.z,
                    plane = adjusted.plane
                )
                actual to MapSquareLoc(resource.loc)
            }
            .groupBy { it.second.id }
            .toSortedMap()
        buffer.encodeMapSquareLocs(sortedLocs)
    }

    private tailrec fun ByteBuffer.decodeMapSquareLocs(entry: MapSquareLocEntryType, locId: Int = -1): MapSquareLocEntryType {
        val offset = gsmarts
        if (offset == 0) {
            return entry
        }
        decodeLocs(entry, locId + offset, 0)
        return decodeMapSquareLocs(entry, locId + offset)
    }

    private tailrec fun ByteBuffer.decodeLocs(entry: MapSquareLocEntryType, locId: Int, packed: Int) {
        val offset = gsmarts
        if (offset == 0) {
            return
        }
        val attributes = g1
        val shape = attributes shr 2
        val rotation = attributes and 0x3
        
        val loc = MapSquareLoc(
            id = locId,
            shape = MapSquareLocShape(shape),
            rotation = MapSquareLocRotation(rotation)
        )

        val coordinates = MapSquareCoordinates(packed + offset - 1)
        val adjustedCoordinates = MapSquareCoordinates(
            x = coordinates.x,
            z = coordinates.z,
            plane = coordinates.plane,
            layer = loc.shape.layer.id
        )
        
        val resource = MapSquareLocResource(adjustedCoordinates.packed, loc.packed)
        require(!entry.locs.contains(resource.packed))
        entry.locs.add(resource.packed)
        return decodeLocs(entry, locId, adjustedCoordinates.packed)
    }

    private tailrec fun ByteBuffer.encodeMapSquareLocs(locs: Map<Int, List<Pair<MapSquareCoordinates, MapSquareLoc>>>, offset: Int = -1, accumulator: Int = 0) {
        val keys = locs.keys
        if (accumulator == keys.size) {
            psmarts(0)
            return
        }
        val key = keys.elementAt(accumulator)
        val group = locs[key]
        requireNotNull(group)
        psmarts(key - offset)
        encodeLocs(group.sortedBy { it.first.packed })
        return encodeMapSquareLocs(locs, key, accumulator + 1)
    }

    private tailrec fun ByteBuffer.encodeLocs(locs: List<Pair<MapSquareCoordinates, MapSquareLoc>>, offset: Int = 0, accumulator: Int = 0) {
        if (accumulator == locs.size) {
            psmarts(0)
            return
        }
        val pair = locs[accumulator]
        val packed = pair.first.packed
        val loc = pair.second
        psmarts(packed - offset + 1)
        val attributes = (loc.shape.id shl 2) or (loc.rotation.id and 0x3)
        p1(attributes)
        return encodeLocs(locs, packed, accumulator + 1)
    }
}
