package kt225.cache.type.config.loc

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readShort
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kt225.cache.archive.Config
import kt225.cache.type.TypeLoader
import kt225.shared.readStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
class LocTypeLoader : TypeLoader<LocType>() {

    init {
        ByteReadPacket(Config.archive.read("loc.dat")).also {
            repeat(it.readUShort().toInt()) { index ->
                entries[index] = it.decode(LocType(index))
            }
        }.release()
    }

    override tailrec fun ByteReadPacket.decode(type: LocType): LocType {
        when (val opcode = readUByte().toInt()) {
            0 -> return type
            1 -> {
                val size = readUByte().toInt()
                type.modelIds = List(size) { 0 }
                type.modelTypes = List(size) { 0 }
                repeat(size) {
                    type.modelIds = type.modelIds.toMutableList().apply { this[it] = readUShort().toInt() }
                    type.modelTypes = type.modelTypes.toMutableList().apply { this[it] = readUByte().toInt() }
                }
            }
            2 -> type.name = readStringCp1252NullTerminated()
            3 -> type.description = readStringCp1252NullTerminated()
            14 -> type.sizeX = readUByte().toInt()
            15 -> type.sizeZ = readUByte().toInt()
            17 -> type.hasCollision = false
            18 -> type.isSolid = false
            19 -> if (readUByte().toInt() == 1) type.interactable = true
            21 -> type.adjustToTerrain = true
            22 -> type.delayShading = true
            23 -> type.culls = true
            24 -> type.animationIndex = readUShort().toInt().let { if (it == 65535) -1 else it }
            25 -> type.disposeAlpha = true
            28 -> type.thickness = readUByte().toInt()
            29 -> type.brightness = readByte().toInt()
            // This is actually 30..39 in the client, but it makes no sense.
            in 30..34 -> type.actions = type.actions.toMutableList().apply {
                this[opcode - 30] = readStringCp1252NullTerminated().let { if (it.equals("Hidden", true)) "null" else it }
            }
            39 -> type.specular = readByte().toInt()
            40 -> {
                val size = readUByte().toInt()
                type.oldColors = List(size) { 0 }
                type.newColors = List(size) { 0 }
                repeat(size) {
                    type.oldColors = type.oldColors.toMutableList().apply { this[it] = readUShort().toInt() }
                    type.newColors = type.newColors.toMutableList().apply { this[it] = readUShort().toInt() }
                }
            }
            60 -> type.mapfunction = readUShort().toInt()
            62 -> type.rotateCounterClockwise = true
            64 -> type.hasShadow = false
            65 -> type.scaleX = readUShort().toInt()
            66 -> type.scaleY = readUShort().toInt()
            67 -> type.scaleZ = readUShort().toInt()
            68 -> type.mapscene = readUShort().toInt()
            69 -> type.interactionSideFlags = readUByte().toInt()
            70 -> type.translateX = readShort().toInt()
            71 -> type.translateY = readShort().toInt()
            72 -> type.translateZ = readShort().toInt()
            73 -> type.obstructsGround = true
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return decode(type)
    }
}
