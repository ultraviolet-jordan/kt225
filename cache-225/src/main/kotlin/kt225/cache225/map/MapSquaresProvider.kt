package kt225.cache225.map

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.map.MapResource
import kt225.cache.map.MapSquares
import kt225.cache.map.Maps
import kt225.common.buffer.bzip2Decompress
import kt225.common.buffer.copy
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.g4
import kt225.common.buffer.gsmarts
import kt225.common.buffer.skip
import java.nio.ByteBuffer

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

                val entry = MapSquareEntryType(mapSquare, type = 0)
                decode(ByteBuffer.wrap(land.bytes).decompress(), entry)
                entry.type = 1
                decode(ByteBuffer.wrap(loc.bytes).decompress(), entry)
                it[landId] = entry
            }
        }
    }

    override fun write(entries: MapSquares<MapSquareEntryType>) {
        TODO("Not yet implemented")
    }

    override fun decode(buffer: ByteBuffer, entry: MapSquareEntryType): MapSquareEntryType {
        if (entry.type == 0) buffer.decodeMapSquareLands(entry) else buffer.decodeMapSquareLocs(entry)
        return entry
    }

    override fun encode(buffer: ByteBuffer, entry: MapSquareEntryType) {
        TODO("Not yet implemented")
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
            val adjustedHeight = if (opcode == 1) g1().let { if (it == 1) 0 else it } else height
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

    private tailrec fun ByteBuffer.decodeMapSquareLocs(entry: MapSquareEntryType, locId: Int = -1) {
        val offset = gsmarts()
        if (offset == 0) {
            return
        }
        decodeLoc(entry, locId + offset, 0)
        return decodeMapSquareLocs(entry, locId + offset)
    }

    private tailrec fun ByteBuffer.decodeLoc(entry: MapSquareEntryType, locId: Int, packedPosition: Int) {
        val offset = gsmarts()
        if (offset == 0) {
            return
        }
        val localPosition = MapSquareLocalPosition(packedPosition + offset - 1)
        val x = localPosition.x
        val z = localPosition.z
        val plane = localPosition.plane.let {
            // Check for bridges.
            val positionAtPlaneOne = MapSquareLocalPosition(1, x, z)
            val land = MapSquareLand(entry.lands[positionAtPlaneOne.packed])
            if (land.collision and 0x2 == 2) it - 1 else it
        }

        if (plane < 0) {
            skip(1) // Discard attributes and continue.
            return decodeLoc(entry, locId, localPosition.packed)
        }

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
        return decodeLoc(entry, locId, localPosition.packed)
    }

    private fun ByteBuffer.decompress(): ByteBuffer {
        val decompressed = g4()
        val buffer = ByteBuffer.wrap(copy().bzip2Decompress())
        require(decompressed == buffer.capacity())
        return buffer
    }
}
