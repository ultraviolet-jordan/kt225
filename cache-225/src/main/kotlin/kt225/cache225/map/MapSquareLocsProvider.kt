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
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocShape
import kt225.common.game.world.map.MapSquarePosition
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
        val lengthX = 0 until maps.maxOf(MapResource::x) + 1
        val lengthZ = 0 until maps.maxOf(MapResource::z) + 1
        for (x in lengthX) {
            for (z in lengthZ) {
                val loc = maps.firstOrNull { it.name == "l${x}_$z" } ?: continue

                val locId = loc.id
                val locX = loc.x
                val locZ = loc.z
                
                val mapSquare = MapSquare(
                    id = locId,
                    x = locX,
                    z = locZ
                )
                
                require(mapSquare.id == locId)
                require(mapSquare.x == locX)
                require(mapSquare.z == locZ)
                
                val entry = MapSquareLocEntryType(mapSquare.packed)
                decode(ByteBuffer.wrap(loc.bytes).decompress(), entry)
                locs[locId] = entry
            }
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
                val adjusted = MapSquarePosition(it.key)
                val actual = MapSquarePosition(
                    x = adjusted.x,
                    z = adjusted.z,
                    plane = adjusted.plane
                )
                actual to MapSquareLoc(it.value)
            }
            // .flatMap { e -> e.mapNotNull { it -> it.let { e.key to MapSquareLoc.create(it) } } }
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

    private tailrec fun ByteBuffer.decodeLocs(entry: MapSquareLocEntryType, locId: Int, packedPosition: Int) {
        val offset = gsmarts
        if (offset == 0) {
            return
        }
        val localPosition = MapSquarePosition(packedPosition + offset - 1)

        // TODO 
        // The client does the commented out logic.
        // We do not do this check here. We need to add this logic
        // when we go to clip the world.

        /*.let {
            // Check for bridges.
            val positionAtPlaneOne = MapSquareLocalPosition(1, x, z)
            val land = MapSquareLand(entry.lands[positionAtPlaneOne.packed])
            if (land.collision and 0x2 == 2) it - 1 else it
        }*/
        /*if (plane < 0) {
            skip(1) // Discard attributes and continue.
            return decodeLocs(entry, locId, localPosition.packed)
        }*/

        val attributes = g1
        val shape = attributes shr 2
        val rotation = attributes and 0x3
        
        val loc = MapSquareLoc(
            id = locId,
//            localX = localPosition.x,
//            localZ = localPosition.z,
//            plane = localPosition.plane,
            shape = MapSquareLocShape(shape),
            rotation = MapSquareLocRotation(rotation)
        )
        
        val adjustedLocalPosition = MapSquarePosition(
            x = localPosition.x,
            z = localPosition.z,
            plane = localPosition.plane,
            layer = loc.shape.layer.id
        )
        
        require(!entry.locs.containsKey(adjustedLocalPosition.packed))
        entry.locs[adjustedLocalPosition.packed] = loc.packed

        // Checks the bitpacking.
        require(loc.id == locId)
//        require(loc.localX == adjustedLocalPosition.x)
//        require(loc.localZ == adjustedLocalPosition.z)
//        require(loc.plane == adjustedLocalPosition.plane)
        require(loc.rotation.id == rotation)
        require(loc.shape.id == shape)
        return decodeLocs(entry, locId, adjustedLocalPosition.packed)
    }

    private tailrec fun ByteBuffer.encodeMapSquareLocs(locs: Map<Int, List<Pair<MapSquarePosition, MapSquareLoc>>>, offset: Int = -1, accumulator: Int = 0) {
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

    private tailrec fun ByteBuffer.encodeLocs(locs: List<Pair<MapSquarePosition, MapSquareLoc>>, offset: Int = 0, accumulator: Int = 0) {
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
