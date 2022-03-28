package kt225.cache.archive.type.config.npc

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kt225.cache.archive.Config
import kt225.cache.archive.type.TypeLoader
import kt225.shared.readStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
class NpcTypeLoader : TypeLoader<NpcType>() {

    init {
        ByteReadPacket(Config.archive.read("npc.dat")).also {
            repeat(it.readUShort().toInt()) { index ->
                entries[index] = it.decode(NpcType(index))
            }
        }.release()
    }

    override tailrec fun ByteReadPacket.decode(type: NpcType): NpcType {
        when (val opcode = readUByte().toInt()) {
            0 -> return type
            1 -> {
                val size = readUByte().toInt()
                type.modelIndices = List(size) { 0 }
                repeat(size) {
                    type.modelIndices = type.modelIndices.toMutableList().apply { this[it] = readUShort().toInt() }
                }
            }
            2 -> type.name = readStringCp1252NullTerminated()
            3 -> type.description = readStringCp1252NullTerminated()
            12 -> type.size = readByte().toInt()
            13 -> type.standSeq = readUShort().toInt()
            14 -> type.walkSeq = readUShort().toInt()
            16 -> type.disposeAlpha = true
            17 -> {
                type.walkSeq = readUShort().toInt()
                type.turnAroundSeq = readUShort().toInt()
                type.turnRightSeq = readUShort().toInt()
                type.turnLeftSeq = readUShort().toInt()
            }
            // This is actually 30..39 in the client, but it makes no sense.
            in 30..34 -> type.options = type.options.toMutableList().apply {
                this[opcode - 30] = readStringCp1252NullTerminated().let { if (it.equals("Hidden", true)) "null" else it }
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
            60 -> {
                val size = readUByte().toInt()
                type.headModelIndices = List(size) { 0 }
                repeat(size) {
                    type.headModelIndices = type.headModelIndices.toMutableList().apply { this[it] = readUShort().toInt() }
                }
            }
            90 -> discard(2) // Unused
            91 -> discard(2) // Unused
            92 -> discard(2) // Unused
            93 -> type.showOnMinimap = false
            95 -> type.level = readUShort().toInt()
            97 -> type.scaleX = readUShort().toInt()
            98 -> type.scaleY = readUShort().toInt()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return decode(type)
    }
}
