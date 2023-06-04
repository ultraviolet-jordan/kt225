package kt225.cache225.config.varp

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.archive.EntryProvider
import kt225.cache.archive.config.ConfigArchive
import kt225.common.buffer.RSByteBuffer
import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
@Singleton
class VarpsProvider @Inject constructor(
    private val configArchive: ConfigArchive
) : EntryProvider<VarpEntry, Varps> {

    override tailrec fun RSByteBuffer.decode(type: VarpEntry): VarpEntry {
        when (val opcode = readUByte()) {
            0 -> return type
            5 -> type.type = readUShort()
            else -> error("Missing opcode $opcode.")
        }
        return decode(type)
    }

    override fun get(): Varps = Varps().also {
        val buffer = configArchive.file("varp.dat")?.buffer() ?: error("varp.dat file not found.")
        repeat(buffer.readUShort()) { varpId ->
            it[varpId] = buffer.decode(VarpEntry(varpId))
        }
    }
}

class Varps : MutableMap<Int, VarpEntry> by TreeMap()
