package kt225.cache225.map

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.map.MapResource
import kt225.cache.map.MapSquares
import kt225.cache.map.Maps
import kt225.common.buffer.decompressBzip2
import kt225.common.buffer.g1
import kt225.common.buffer.g1s
import kt225.common.buffer.gSmart1or2
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class MapSquaresProvider @Inject constructor(
    private val maps: Maps
) : EntryProvider<MapSquareEntryType, MapSquares<MapSquareEntryType>> {
    override fun get(): MapSquares<MapSquareEntryType> = MapSquares<MapSquareEntryType>().also {
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

                val packedEntry = MapSquareEntry(landId, landX, landZ)
                require(packedEntry.id == landId)
                require(packedEntry.x == landX)
                require(packedEntry.z == landZ)

                val entry = MapSquareEntryType(packedEntry, type = 0)
                decode(ByteBuffer.wrap(land.bytes).decompressBzip2(land.bytes.size - 4, 4), entry)
                entry.type = 1
                decode(ByteBuffer.wrap(loc.bytes).decompressBzip2(loc.bytes.size - 4, 4), entry)
                it[landId] = entry
            }
        }
    }

    override fun decode(buffer: ByteBuffer, entry: MapSquareEntryType): MapSquareEntryType {
        if (entry.type == 0) buffer.decodeMapSquareLands(entry) else buffer.decodeMapSquareLocs(entry)
        return entry
    }

    override fun encode(entry: MapSquareEntryType): ByteBuffer {
        TODO("Not yet implemented")
    }

    private fun ByteBuffer.decodeMapSquareLands(entry: MapSquareEntryType) {
        for (plane in 0 until 4) {
            for (x in 0 until 64) {
                for (z in 0 until 64) {
                    entry.lands[(x and 0x3f shl 6) or (z and 0x3f) or (plane shl 12)] = loadTerrain()
                }
            }
        }
    }

    private tailrec fun ByteBuffer.loadTerrain(
        height: Int = 0,
        overlayId: Int = 0,
        overlayPath: Int = 0,
        overlayRotation: Int = 0,
        flags: Int = 0,
        underlayId: Int = 0
    ): MapSquareLandTile = when (val opcode = g1()) {
        0, 1 -> { // This isn't perfect according to the client code but i cba.
            MapSquareLandTile(
                height = if (opcode == 1) g1().let { if (it == 1) 0 else it } else height,
                overlayId = overlayId,
                overlayPath = overlayPath,
                overlayRotation = overlayRotation,
                collision = flags,
                underlayId = underlayId
            )
        }
        else -> loadTerrain(
            height = height,
            overlayId = if (opcode in 2..49) g1s() else overlayId,
            overlayPath = if (opcode in 2..49) (opcode - 2) / 4 else overlayPath,
            overlayRotation = if (opcode in 2..49) opcode - 2 and 3 else overlayRotation,
            flags = if (opcode in 50..81) opcode - 49 else flags,
            underlayId = if (opcode > 81) opcode - 81 else underlayId
        )
    }

    private fun ByteBuffer.decodeMapSquareLocs(entry: MapSquareEntryType, locId: Int = -1) {
        val offset = gSmart1or2()
        if (offset == 0) return
        decodeLoc(entry, locId + offset, 0)
        return decodeMapSquareLocs(entry, locId + offset)
    }

    private tailrec fun ByteBuffer.decodeLoc(entry: MapSquareEntryType, locId: Int, packedLocation: Int) {
        val offset = gSmart1or2()
        if (offset == 0) return
        val attributes = g1()
        val shape = attributes shr 2
        val rotation = attributes and 0x3

        val packed = packedLocation + offset - 1
        val x = packed shr 6 and 0x3f
        val z = packed and 0x3f
        val plane = (packed shr 12).let {
            // Check for bridges.
            if (entry.lands[(x and 0x3f shl 6) or (z and 0x3f) or (1 shl 12)]!!.collision and 0x2 == 2) it - 1 else it
        }
        // New adjusted packed location after adjusting for bridge.
        val adjusted = (x and 0x3f shl 6) or (z and 0x3f) or (plane shl 12)

        if (plane >= 0) {
            entry.locs[adjusted] = when (val size = entry.locs[adjusted]?.size ?: 0) {
                0 -> Array(1) { MapSquareLocTile(locId, x, z, plane, shape, rotation) }
                in 1 until 5 -> {
                    entry.locs[adjusted]!!.copyOf(size + 1).also {
                        it[size] = MapSquareLocTile(locId, x, z, plane, shape, rotation)
                    }
                }
                else -> throw AssertionError("Size is too many. 5 capacity.")
            }
        }
        return decodeLoc(entry, locId, packed)
    }
}
