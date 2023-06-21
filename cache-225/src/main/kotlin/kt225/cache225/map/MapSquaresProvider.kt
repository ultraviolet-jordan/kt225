package kt225.cache225.map

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.bzip2.bzip2Compress
import kt225.cache.bzip2.bzip2Decompress
import kt225.cache.map.MapResource
import kt225.cache.map.MapSquares
import kt225.cache.map.Maps
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.g4
import kt225.common.buffer.gdata
import kt225.common.buffer.gsmarts
import kt225.common.buffer.p1
import kt225.common.buffer.p4
import kt225.common.buffer.pdata
import kt225.common.buffer.psmarts
import java.nio.ByteBuffer
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
@Singleton
class MapSquaresProvider @Inject constructor(
    private val maps: Maps
) : EntryProvider<MapSquareEntryType, MapSquares<MapSquareEntryType>> {
    override fun read(): MapSquares<MapSquareEntryType> = MapSquares<MapSquareEntryType>().also {
        val lengthX = 0 until maps.maxOf(MapResource::x) + 1
        val lengthZ = 0 until maps.maxOf(MapResource::z) + 1

        for (x in lengthX) {
            for (z in lengthZ) {
                val land = maps.firstOrNull { m -> m.name == "m${x}_$z" } ?: continue
                val loc = maps.firstOrNull { l -> l.name == "l${x}_$z" } ?: continue

                val landId = land.id
                val landX = land.x
                val landZ = land.z

                require(landId == loc.id)
                require(landX == loc.x)
                require(landZ == loc.z)

                val mapSquare = MapSquare(landId, landX, landZ)
                require(mapSquare.id == landId)
                require(mapSquare.x == landX)
                require(mapSquare.z == landZ)

                val entry = MapSquareEntryType(mapSquare.packed, type = 0)
                decode(ByteBuffer.wrap(land.bytes).decompress(), entry)
                entry.type = 1
                decode(ByteBuffer.wrap(loc.bytes).decompress(), entry)
                it[landId] = entry
            }
        }
    }

    override fun write(entries: MapSquares<MapSquareEntryType>) {
        entries.values.forEach { entry ->
            val mapSquare = MapSquare(entry.mapSquare)
            val id = mapSquare.id
            val x = mapSquare.x
            val z = mapSquare.z

            val landName = "m${x}_$z"
            val locName = "l${x}_$z"

            val landBuffer = ByteBuffer.allocate(100_000)
            entry.type = 0
            encode(landBuffer, entry)
            val locBuffer = ByteBuffer.allocate(100_000)
            entry.type = 1
            encode(locBuffer, entry)

            landBuffer.flip()
            locBuffer.flip()
            
            val landBytes = landBuffer.compress()
            val locBytes = locBuffer.compress()
            
            val landCrc = CRC32().also { it.update(landBytes) }.value.toInt()
            val locCrc = CRC32().also { it.update(locBytes) }.value.toInt()
            
            val existingLand = maps.firstOrNull { it.id == id && it.name == landName }
            val existingLandIndex = maps.indexOf(existingLand)
            if (existingLandIndex != -1) {
                maps[existingLandIndex] = MapResource(landName, id, x, z, 0, landBytes, landCrc)
            }
            val existingLoc = maps.firstOrNull { it.id == id && it.name == locName }
            val existingLocIndex = maps.indexOf(existingLoc)
            if (existingLocIndex != -1) {
                maps[existingLocIndex] = MapResource(locName, id, x, z, 1, locBytes, locCrc)
            }
        }
    }

    override fun decode(buffer: ByteBuffer, entry: MapSquareEntryType): MapSquareEntryType {
        if (entry.type == 0) {
            buffer.decodeMapSquareLands(entry)
        } else {
            buffer.decodeMapSquareLocs(entry)
        }
        return entry
    }

    override fun encode(buffer: ByteBuffer, entry: MapSquareEntryType) {
        if (entry.type == 1) {
            // We have to group the locs together in a specific way for it to encode properly.
            // Locs have to be grouped together by their packed position.
            // Then sorted in order of the packed position.
            // Then sorted in order by their locId.
            val sortedLocs = entry
                .locs
                .flatMap { e -> e.value.map { e.key to MapSquareLoc(it) } }
                .groupBy { it.second.id }
                .toSortedMap()
            buffer.encodeMapSquareLocs(sortedLocs)
        } else {
            buffer.encodeMapSquareLands(entry.lands)
        }
    }

    private fun ByteBuffer.decodeMapSquareLands(entry: MapSquareEntryType) {
        for (plane in 0 until 4) {
            for (x in 0 until 64) {
                for (z in 0 until 64) {
                    val localPosition = MapSquareLocalPosition(plane, x, z)
                    entry.lands[localPosition.packed] = decodeLand().packed
                }
            }
        }
    }
    
    private fun ByteBuffer.encodeMapSquareLands(lands: LongArray) {
        for (plane in 0 until 4) {
            for (x in 0 until 64) {
                for (z in 0 until 64) {
                    val localPosition = MapSquareLocalPosition(plane, x, z)
                    encodeLand(MapSquareLand(lands[localPosition.packed]))
                }
            }
        }
    }

    private tailrec fun ByteBuffer.decodeLand(
        height: Int = 0,
        overlayId: Int = 0,
        overlayPath: Int = 0,
        overlayRotation: Int = 0,
        collision: Int = 0,
        underlayId: Int = 0
    ): MapSquareLand {
        val opcode = g1()
        if (opcode == 0 || opcode == 1) {
            val adjustedHeight = if (opcode == 1) g1()/*.let { if (it == 1) 0 else it }*/ else height
            val land = MapSquareLand(adjustedHeight, overlayId, overlayPath, overlayRotation, collision, underlayId)

            // Checks the bitpacking.
            require(land.height == adjustedHeight)
            require(land.overlayId == overlayId)
            require(land.overlayPath == overlayPath)
            require(land.overlayRotation == overlayRotation)
            require(land.collision == collision)
            require(land.underlayId == underlayId)
            return land
        }
        return decodeLand(
            height = height,
            overlayId = if (opcode in 2..49) g1b() else overlayId,
            overlayPath = if (opcode in 2..49) (opcode - 2) / 4 else overlayPath,
            overlayRotation = if (opcode in 2..49) opcode - 2 and 3 else overlayRotation,
            collision = if (opcode in 50..81) opcode - 49 else collision,
            underlayId = if (opcode > 81) opcode - 81 else underlayId
        )
    }
    
    private fun ByteBuffer.encodeLand(land: MapSquareLand) {
        if (land.overlayId != 0) {
            val path = land.overlayPath * 4 + 2
            val rotation = land.overlayRotation and 0x3
            val opcode = path + rotation
            p1(opcode)
            p1(land.overlayId)
        }
        if (land.collision != 0) {
            p1(land.collision + 49)
        }
        if (land.underlayId != 0) {
            p1(land.underlayId + 81)
        }
        if (land.height != 0) {
            p1(1)
            p1(land.height)
        } else {
            p1(0)
        }
    }

    private tailrec fun ByteBuffer.decodeMapSquareLocs(entry: MapSquareEntryType, locId: Int = -1) {
        val offset = gsmarts()
        if (offset == 0) {
            return
        }
        decodeLocs(entry, locId + offset, 0)
        return decodeMapSquareLocs(entry, locId + offset)
    }

    private tailrec fun ByteBuffer.encodeMapSquareLocs(locs: Map<Int, List<Pair<Int, MapSquareLoc>>>, offset: Int = -1, accumulator: Int = 0) {
        val keys = locs.keys
        if (accumulator == keys.size) {
            psmarts(0)
            return
        }
        val key = keys.elementAt(accumulator)
        val group = locs[key]
        requireNotNull(group)
        psmarts(key - offset)
        encodeLocs(group.sortedBy(Pair<Int, MapSquareLoc>::first))
        return encodeMapSquareLocs(locs, key, accumulator + 1)
    }

    private tailrec fun ByteBuffer.decodeLocs(entry: MapSquareEntryType, locId: Int, packedPosition: Int) {
        val offset = gsmarts()
        if (offset == 0) {
            return
        }
        val localPosition = MapSquareLocalPosition(packedPosition + offset - 1)
        val x = localPosition.x
        val z = localPosition.z
        val plane = localPosition.plane
        
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

        val attributes = g1()
        val shape = attributes shr 2
        val rotation = attributes and 0x3
        val loc = MapSquareLoc(locId, x, z, plane, shape, rotation)

        // New adjusted packed location after adjusting for bridge.
        val adjustedLocalPosition = MapSquareLocalPosition(plane, x, z).packed
        // Dynamically increase the slot on this tile depending on the incoming data.
        // There is a limit of 5 slots per tile.
        val slots = entry.locs[adjustedLocalPosition]?.size ?: 0
        require(slots < 5)
        entry.locs[adjustedLocalPosition] = entry.locs[adjustedLocalPosition]?.copyOf(slots + 1)?.also {
            it[slots] = loc.packed
        } ?: LongArray(1) { loc.packed }

        // Checks the bitpacking.
        require(loc.id == locId)
        require(loc.x == x)
        require(loc.z == z)
        require(loc.plane == plane)
        require(loc.rotation == rotation)
        require(loc.shape == shape)
        return decodeLocs(entry, locId, localPosition.packed)
    }

    private tailrec fun ByteBuffer.encodeLocs(locs: List<Pair<Int, MapSquareLoc>>, offset: Int = 0, accumulator: Int = 0) {
        if (accumulator == locs.size) {
            psmarts(0)
            return
        }
        val pair = locs[accumulator]
        val loc = pair.second
        val localPosition = MapSquareLocalPosition(loc.plane, loc.x, loc.z).packed - offset + 1
        psmarts(localPosition)
        val attributes = (loc.shape shl 2) or (loc.rotation and 0x3)
        p1(attributes)
        return encodeLocs(locs, pair.first, accumulator + 1)
    }

    private fun ByteBuffer.decompress(): ByteBuffer {
        val decompressed = g4()
        val buffer = ByteBuffer.wrap(bzip2Decompress(gdata(limit() - 4)))
        require(decompressed == buffer.limit())
        return buffer
    }

    private fun ByteBuffer.compress(): ByteArray {
        val bytes = gdata()
        val compressed = bzip2Compress(bytes)
        val buffer = ByteBuffer.allocate(compressed.size + 4)
        buffer.p4(bytes.size)
        buffer.pdata(compressed)
        buffer.flip()
        return buffer.gdata()
    }
}
