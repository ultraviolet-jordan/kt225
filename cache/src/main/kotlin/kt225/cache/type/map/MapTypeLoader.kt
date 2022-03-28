package kt225.cache.type.map

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import kt225.cache.decompressBzip2
import kt225.cache.maps
import kt225.cache.type.TypeLoader
import kt225.shared.readUShortSmart

/**
 * @author Jordan Abraham
 */
class MapTypeLoader : TypeLoader<MapType>() {

    init {
        repeat(100) { regionX ->
            repeat(256) { regionZ ->
                val regionId = (regionX shl 8) or regionZ

                // Decode "mXX_YY".
                val map = maps["m${regionX}_$regionZ"]?.clone() ?: return@repeat
                val mapDataSize = ByteReadPacket(map).readInt()
                val mapData = ByteArray(mapDataSize)
                map.decompressBzip2(mapData, mapDataSize, map.size - 4, 4)
                val mapType = ByteReadPacket(mapData).decode(MapType(regionId, regionX, regionZ))

                // Decode "lXX_YY".
                val loc = maps["l${regionX}_$regionZ"]?.clone() ?: return@repeat
                val locDataSize = ByteReadPacket(loc).readInt()
                val locData = ByteArray(locDataSize)
                loc.decompressBzip2(locData, locDataSize, loc.size - 4, 4)
                ByteReadPacket(locData).decodeLocs(mapType)

                entries[regionId] = mapType
            }
        }
    }

    override fun ByteReadPacket.decode(type: MapType): MapType {
        for (level in 0 until 4) {
            for (x in 0 until 64) {
                for (z in 0 until 64) {
                    return decodeCollisions(type, level, x, z)
                }
            }
        }
        return type
    }

    private tailrec fun ByteReadPacket.decodeCollisions(type: MapType, level: Int, x: Int, z: Int): MapType {
        when (val opcode = readUByte().toInt()) {
            0 -> return type
            1 -> {
                discard(1)
                return type
            }
            in 2..49 -> discard(1)
            in 50..81 -> type.collision[level][x][z] = ((opcode - 49).toByte())
        }
        return decodeCollisions(type, level, x, z)
    }

    private fun ByteReadPacket.decodeLocs(type: MapType) {
        var objectId = -1
        var offset: Int
        while (readUShortSmart().also { offset = it } != 0) {
            objectId += offset
            var packed = 0
            var locOffset: Int
            while (readUShortSmart().also { locOffset = it } != 0) {
                packed += locOffset - 1
                val localX = packed shr 6 and 0x3f
                val localZ = packed and 0x3f
                var level = packed shr 12
                val attributes = readUByte().toInt()
                val shape = attributes shr 2
                val rotation = attributes and 0x3
                if (type.collision[1][localX][localZ].toInt() and 2 == 2) {
                    level--
                }
                if (level < 0) continue
                type.locs[level][localX][localZ].add(
                    MapType.MapLocType(
                        id = objectId,
                        x = localX,
                        z = localZ,
                        level = level,
                        shape = shape,
                        rotation = rotation
                    )
                )
            }
        }
    }
}
