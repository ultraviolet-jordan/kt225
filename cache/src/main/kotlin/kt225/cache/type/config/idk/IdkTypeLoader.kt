package kt225.cache.type.config.idk

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kt225.cache.archive.Config
import kt225.cache.type.TypeLoader

/**
 * @author Jordan Abraham
 */
class IdkTypeLoader : TypeLoader<IdkType>() {

    init {
        ByteReadPacket(Config.archive.read("idk.dat")).also {
            repeat(it.readUShort().toInt()) { index ->
                entries[index] = it.decode(IdkType(index))
            }
        }.release()
    }

    override tailrec fun ByteReadPacket.decode(type: IdkType): IdkType {
        when (val opcode = readUByte().toInt()) {
            0 -> return type
            1 -> type.type = readUByte().toInt()
            2 -> {
                val size = readUByte().toInt()
                type.modelIds = List(size) { 0 }
                repeat(size) {
                    type.modelIds = type.modelIds.toMutableList().apply { this[it] = readUShort().toInt() }
                }
            }
            3 -> type.validStyle = true
            // This is 40..49 in the client.
            in 40..46 -> type.oldColors = type.oldColors.toMutableList().apply { this[opcode - 40] = readUShort().toInt() }
            // This is 50..59 in the client.
            in 50..56 -> type.newColors = type.newColors.toMutableList().apply { this[opcode - 50] = readUShort().toInt() }
            // This is 60..69 in the client.
            in 60..65 -> type.headModelIds = type.headModelIds.toMutableList().apply { this[opcode - 60] = readUShort().toInt() }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return decode(type)
    }
}
