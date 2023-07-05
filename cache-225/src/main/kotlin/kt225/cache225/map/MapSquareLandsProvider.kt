@file:Suppress("DuplicatedCode")

package kt225.cache225.map

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.map.MapLands
import kt225.cache.map.MapResource
import kt225.cache.map.MapSquareLands
import kt225.common.buffer.flip
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.p1
import kt225.common.game.world.map.MapSquare
import kt225.common.game.world.map.MapSquareCoordinates
import kt225.common.game.world.map.MapSquareLand
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

        maps.filter { it.name.startsWith("m") }.parallelStream().forEach {
            val mapSquare = MapSquare(it.id, it.x, it.z)
            val entry = MapSquareLandEntryType(mapSquare.packed)
            decode(ByteBuffer.wrap(it.bytes).decompress(), entry)
            lands[it.id] = entry
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

            val compressed = buffer.flip.compress()
            val crc = CRC32().also { it.update(compressed) }.value.toInt()
            val existing = maps.firstOrNull { it.id == id && it.name == name }
            val index = maps.indexOf(existing)
            if (index != -1) {
                maps[index] = MapResource(name, id, x, z, compressed, crc)
            }
        }
    }

    override fun decode(buffer: ByteBuffer, entry: MapSquareLandEntryType): MapSquareLandEntryType {
        val area = MapSquare.AREA
        for (index in 0 until 4 * area) {
            val remaining = index % area
            val mapSquareCoordinates = MapSquareCoordinates(remaining / 64, remaining % 64, index / area)
            entry.lands[mapSquareCoordinates.packed] = buffer.decodeLand().packed
        }
        return entry
    }

    override fun encode(buffer: ByteBuffer, entry: MapSquareLandEntryType) {
        val area = MapSquare.AREA
        for (index in 0 until 4 * area) {
            val remaining = index % area
            val mapSquareCoordinates = MapSquareCoordinates(remaining / 64, remaining % 64, index / area)
            buffer.encodeLand(MapSquareLand(entry.lands[mapSquareCoordinates.packed]))
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
            return MapSquareLand(adjustedHeight, overlayId, overlayPath, overlayRotation, collision, underlayId)
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
