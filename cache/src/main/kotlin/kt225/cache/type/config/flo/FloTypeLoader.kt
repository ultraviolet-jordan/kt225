package kt225.cache.type.config.flo

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kt225.cache.archive.Config
import kt225.cache.type.TypeLoader
import kt225.shared.readStringCp1252NullTerminated
import kt225.shared.readUMedium

/**
 * @author Jordan Abraham
 */
class FloTypeLoader : TypeLoader<FloType>() {

    init {
        ByteReadPacket(Config.archive.read("flo.dat")).also {
            repeat(it.readUShort().toInt()) { index ->
                entries[index] = it.decode(FloType(index))
            }
        }.release()
    }

    override tailrec fun ByteReadPacket.decode(type: FloType): FloType {
        when (val opcode = readUByte().toInt()) {
            0 -> return type
            1 -> {
                type.rgb = readUMedium()
                type.setColor()
            }
            2 -> type.textureId = readUByte().toInt()
            3 -> { /* Do nothing.*/ }
            5 -> type.occlude = false
            6 -> type.name = readStringCp1252NullTerminated()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return decode(type)
    }
}
