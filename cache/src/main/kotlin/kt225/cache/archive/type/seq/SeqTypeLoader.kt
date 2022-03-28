package kt225.cache.archive.type.seq

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kt225.cache.archive.Config
import kt225.cache.archive.type.TypeLoader

/**
 * @author Jordan Abraham
 */
class SeqTypeLoader : TypeLoader<SeqType>() {
    override fun load(): Map<Int, SeqType> = buildMap {
        val buffer = ByteReadPacket(Config.archive.read("seq.dat"))
        repeat(buffer.readUShort().toInt()) {
            put(it, buffer.decode(SeqType(it)))
        }
        buffer.release()
    }

    override tailrec fun ByteReadPacket.decode(type: SeqType): SeqType {
        when (val opcode = readUByte().toInt()) {
            0 -> return type
            1 -> repeat(readUByte().toInt()) {
                discard(2) // skip primaryFrames
                discard(2) // skip secondaryFrames
                discard(2) // skip frameDelay
            }
            2 -> type.delay = readUShort().toInt()
            3 -> repeat(readUByte().toInt()) {
                discard(1) // skip labelGroups
            }
            4 -> type.renderPadding = true
            5 -> type.priority = readUByte().toInt()
            6 -> type.shieldOverride = readUShort().toInt()
            7 -> type.weaponOverride = readUShort().toInt()
            8 -> type.replays = readUByte().toInt()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return decode(type)
    }
}
