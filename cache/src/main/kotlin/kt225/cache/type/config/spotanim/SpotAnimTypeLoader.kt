package kt225.cache.type.config.spotanim

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kt225.cache.archive.Config
import kt225.cache.type.TypeLoader

/**
 * @author Jordan Abraham
 */
class SpotAnimTypeLoader : TypeLoader<SpotAnimType>() {

    init {
        ByteReadPacket(Config.archive.read("spotanim.dat")).also {
            repeat(it.readUShort().toInt()) { index ->
                entries[index] = it.decode(SpotAnimType(index))
            }
        }.release()
    }

    override tailrec fun ByteReadPacket.decode(type: SpotAnimType): SpotAnimType {
        when (val opcode = readUByte().toInt()) {
            0 -> return type
            1 -> type.modelIndex = readUShort().toInt()
            2 -> type.sequenceId = readUShort().toInt()
            3 -> type.disposeAlpha = true
            4 -> type.breadthScale = readUShort().toInt()
            5 -> type.depthScale = readUShort().toInt()
            6 -> type.orientation = readUShort().toInt()
            7 -> type.ambience = readUByte().toInt()
            8 -> type.modelShadow = readUByte().toInt()
            // This is 40..49 in the client.
            in 40..46 -> type.oldColors = type.oldColors.toMutableList().apply { this[opcode - 40] = readUShort().toInt() }
            // This is 50..59 in the client.
            in 50..56 -> type.newColors = type.newColors.toMutableList().apply { this[opcode - 50] = readUShort().toInt() }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return decode(type)
    }
}
