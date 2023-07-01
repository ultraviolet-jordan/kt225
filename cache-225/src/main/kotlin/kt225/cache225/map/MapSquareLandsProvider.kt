@file:Suppress("DuplicatedCode")

package kt225.cache225.map

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.map.MapLands
import kt225.cache.map.MapResource
import kt225.cache.map.MapSquareLands
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.p1
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareLand
import kt225.common.game.world.map.MapSquarePosition
import java.nio.ByteBuffer
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
@Singleton
class MapSquareLandsProvider @Inject constructor(
    private val maps: MapLands
) : MapSquaresProvider<MapSquareLandEntryType, MapSquareLands<MapSquareLandEntryType>> {
    override fun get(): MapSquareLands<MapSquareLandEntryType> {
        val lands = MapSquareLands<MapSquareLandEntryType>()
        val lengthX = 0 until maps.maxOf(MapResource::x) + 1
        val lengthZ = 0 until maps.maxOf(MapResource::z) + 1
        for (x in lengthX) {
            for (z in lengthZ) {
                val land = maps.firstOrNull { it.name == "m${x}_$z" } ?: continue

                val landId = land.id
                val landX = land.x
                val landZ = land.z
                
                val mapSquare = MapSquare(
                    id = landId,
                    x = landX,
                    z = landZ
                )
                
                require(mapSquare.id == landId)
                require(mapSquare.x == landX)
                require(mapSquare.z == landZ)
                
                val entry = MapSquareLandEntryType(mapSquare.packed)
                decode(ByteBuffer.wrap(land.bytes).decompress(), entry)
                lands[landId] = entry
            }
        }
        return lands
    }

    override fun write(entries: MapSquareLands<MapSquareLandEntryType>) {
        entries.values.forEach { entry ->
            val mapSquare = MapSquare(entry.mapSquare)
            val id = mapSquare.id
            val x = mapSquare.x
            val z = mapSquare.z
            val name = "m${x}_$z"

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

    override fun decode(buffer: ByteBuffer, entry: MapSquareLandEntryType): MapSquareLandEntryType {
        for (plane in 0 until 4) {
            for (x in 0 until 64) {
                for (z in 0 until 64) {
                    val mapSquarePosition = MapSquarePosition(x, z, plane)
                    entry.lands[mapSquarePosition.packed] = buffer.decodeLand().packed
                }
            }
        }
        return entry
    }

    override fun encode(buffer: ByteBuffer, entry: MapSquareLandEntryType) {
        for (plane in 0 until 4) {
            for (x in 0 until 64) {
                for (z in 0 until 64) {
                    val mapSquarePosition = MapSquarePosition(x, z, plane)
                    buffer.encodeLand(MapSquareLand(entry.lands[mapSquarePosition.packed]))
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
        val opcode = g1
        if (opcode == 0 || opcode == 1) {
            val adjustedHeight = if (opcode == 1) g1/*.let { if (it == 1) 0 else it }*/ else height
            
            val land = MapSquareLand(
                height = adjustedHeight,
                overlayId = overlayId,
                overlayPath = overlayPath,
                overlayRotation = overlayRotation,
                collision = collision,
                underlayId = underlayId
            )

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
            overlayId = if (opcode in 2..49) g1b else overlayId,
            overlayPath = if (opcode in 2..49) (opcode - 2) / 4 else overlayPath,
            overlayRotation = if (opcode in 2..49) opcode - 2 and 3 else overlayRotation,
            collision = if (opcode in 50..81) opcode - 49 else collision,
            underlayId = if (opcode > 81) opcode - 81 else underlayId
        )
    }

    private fun ByteBuffer.encodeLand(land: MapSquareLand) {
        pNotZero(land.overlayId, (land.overlayPath * 4 + 2) + (land.overlayRotation and 0x3), ByteBuffer::p1)
        pNotZero(land.collision, land.collision + 49) {}
        pNotZero(land.underlayId, land.underlayId + 81) {}
        pNotZero(land.height, 1, ByteBuffer::p1)
        if (land.height == 0) {
            p1(0)
        }
    }
}
