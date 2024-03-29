@file:Suppress("LocalVariableName")

package kt225.cache225.config.idk

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.config.Config
import kt225.cache.config.idk.Idks
import kt225.common.buffer.flip
import kt225.common.buffer.g1
import kt225.common.buffer.g2
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class IdksProvider @Inject constructor(
    private val config: Config
) : EntryProvider<IdkEntryType, Idks<IdkEntryType>> {
    override fun get(): Idks<IdkEntryType> {
        val buffer = config.read("idk.dat") ?: error("idk.dat file not found.")
        val idks = Idks<IdkEntryType>()
        repeat(buffer.g2) {
            idks[it] = decode(buffer, IdkEntryType(it))
        }
        return idks
    }
    
    override fun write(entries: Idks<IdkEntryType>) {
        val buffer = ByteBuffer.allocate(100_000)
        buffer.p2(entries.size)
        entries.values.forEach {
            encode(buffer, it)
        }
        config.add("idk.dat", buffer.flip)
    }
    
    override tailrec fun decode(buffer: ByteBuffer, entry: IdkEntryType): IdkEntryType {
        when (val opcode = buffer.g1) {
            0 -> return entry
            1 -> entry.type = buffer.g1
            2 -> entry.models = IntArray(buffer.g1) { buffer.g2 }
            3 -> entry.disable = true
            in 40..49 -> entry.recol_s[opcode - 40] = buffer.g2
            in 50..59 -> entry.recol_d[opcode - 50] = buffer.g2
            in 60..69 -> entry.headModels[opcode - 60] = buffer.g2
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(buffer: ByteBuffer, entry: IdkEntryType) {
        buffer.pNotNegative1(entry.type, 1, ByteBuffer::p1)
        buffer.pNotNull(entry.models, 2) {
            p1(it.size)
            it.forEach(buffer::p2)
        }
        buffer.pTrue(entry.disable, 3)
        entry.recol_s.forEachIndexed { index, recol_s ->
            buffer.pNotZero(recol_s, index + 40, ByteBuffer::p2)
        }
        entry.recol_d.forEachIndexed { index, recol_d ->
            buffer.pNotZero(recol_d, index + 50, ByteBuffer::p2)
        }
        entry.headModels.forEachIndexed { index, headModel ->
            buffer.pNotNegative1(headModel, index + 60, ByteBuffer::p2)
        }
        buffer.p1(0)
    }
}
