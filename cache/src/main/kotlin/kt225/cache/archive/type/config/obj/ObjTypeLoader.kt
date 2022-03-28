package kt225.cache.archive.type.config.obj

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kt225.cache.archive.Config
import kt225.cache.archive.type.TypeLoader
import kt225.shared.readStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
class ObjTypeLoader : TypeLoader<ObjType>() {

    init {
        ByteReadPacket(Config.archive.read("obj.dat")).also {
            repeat(it.readUShort().toInt()) { index ->
                entries[index] = it.decode(ObjType(index))
            }
        }.release()
    }

    override tailrec fun ByteReadPacket.decode(type: ObjType): ObjType {
        when (val opcode = readUByte().toInt()) {
            0 -> return type
            1 -> type.modelIndex = readUShort().toInt()
            2 -> type.name = readStringCp1252NullTerminated()
            3 -> type.description = readStringCp1252NullTerminated()
            4 -> type.iconZoom = readUShort().toInt()
            5 -> type.iconCameraPitch = readUShort().toInt()
            6 -> type.iconYaw = readUShort().toInt()
            7 -> type.iconX = readUShort().toInt().let { if (it > Short.MAX_VALUE) it - 0x10000 else it }
            8 -> type.iconY = readUShort().toInt().let { if (it > Short.MAX_VALUE) it - 0x10000 else it }
            9 -> type.aBoolean155 = true // Unused.
            10 -> discard(2) // Unused.
            11 -> type.stackable = true
            12 -> type.value = readInt()
            16 -> type.members = true
            23 -> {
                type.maleModel0 = readUShort().toInt()
                type.maleOffsetY = readByte().toInt()
            }
            24 -> type.maleModel1 - readUShort().toInt()
            25 -> {
                type.femaleModel0 = readUShort().toInt()
                type.femaleOffsetY = readByte().toInt()
            }
            26 -> type.femaleModel1 = readUShort().toInt()
            in 30..34 -> type.groundOptions = type.groundOptions.toMutableList().apply {
                this[opcode - 30] = readStringCp1252NullTerminated().let { if (it.equals("Hidden", true)) "null" else it }
            }
            in 35..39 -> type.options = type.options.toMutableList().apply {
                this[opcode - 35] = readStringCp1252NullTerminated()
            }
            40 -> {
                val size = readUByte().toInt()
                type.oldColors = List(size) { 0 }
                type.newColors = List(size) { 0 }
                repeat(size) {
                    type.oldColors = type.oldColors.toMutableList().apply { this[it] = readUShort().toInt() }
                    type.newColors = type.newColors.toMutableList().apply { this[it] = readUShort().toInt() }
                }
            }
            78 -> type.maleModel2 = readUShort().toInt()
            79 -> type.femaleModel2 = readUShort().toInt()
            90 -> type.maleHeadModelA = readUShort().toInt()
            91 -> type.femaleHeadModelA = readUShort().toInt()
            92 -> type.maleHeadModelB = readUShort().toInt()
            93 -> type.femaleHeadModelB = readUShort().toInt()
            95 -> type.iconRoll = readUShort().toInt()
            97 -> type.linkedId = readUShort().toInt()
            98 -> type.certificateId = readUShort().toInt()
            in 100..109 -> {
                if (type.stackId.isEmpty()) {
                    type.stackId = List(10) { 0 }
                    type.stackAmount = List(10) { 0 }
                }
                type.stackId = type.stackId.toMutableList().apply { this[opcode - 100] = readUShort().toInt() }
                type.stackAmount = type.stackAmount.toMutableList().apply { this[opcode - 100] = readUShort().toInt() }
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return decode(type)
    }
}
