package kt225.cache.type.config.varp

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kt225.cache.archive.Config
import kt225.cache.type.TypeLoader

/**
 * @author Jordan Abraham
 */
class VarpTypeLoader : TypeLoader<VarpType>() {

    init {
        ByteReadPacket(Config.archive.read("varp.dat")).also {
            repeat(it.readUShort().toInt()) { index ->
                entries[index] = it.decode(VarpType(index))
            }
        }.release()
    }

    override tailrec fun ByteReadPacket.decode(type: VarpType): VarpType {
        when (val opcode = readUByte().toInt()) {
            0 -> return type
            5 -> type.type = readUShort().toInt()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return decode(type)
    }
}
