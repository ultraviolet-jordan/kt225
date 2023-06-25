package kt225.cache225.config.idk

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.config.Config
import kt225.cache.config.idk.Idks
import kt225.cache225.config.pNotNegative1
import kt225.cache225.config.pTrue
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
    override fun read(): Idks<IdkEntryType> {
        val buffer = config.read("idk.dat") ?: error("idk.dat file not found.")
        val idk = Idks<IdkEntryType>()
        repeat(buffer.g2()) {
            idk[it] = decode(buffer, IdkEntryType(it))
        }
        return idk
    }
    
    override fun write(entries: Idks<IdkEntryType>) {
        val length = entries.size
        val buffer = ByteBuffer.allocate(100_000)
        buffer.p2(length)
        entries.values.forEach {
            encode(buffer, it)
        }
        buffer.flip()
        config.add("idk.dat", buffer)
    }
    
    override tailrec fun decode(buffer: ByteBuffer, entry: IdkEntryType): IdkEntryType {
        when (val opcode = buffer.g1()) {
            0 -> return entry
            1 -> entry.type = buffer.g1()
            2 -> entry.models = IntArray(buffer.g1()) { buffer.g2() }
            3 -> entry.disable = true
            in 40..49 -> entry.recol_s[opcode - 40] = buffer.g2()
            in 50..59 -> entry.recol_d[opcode - 50] = buffer.g2()
            in 60..69 -> entry.headModels[opcode - 60] = buffer.g2()
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(buffer: ByteBuffer, entry: IdkEntryType) {
        buffer.pNotNegative1(entry.type, 1, ByteBuffer::p1)
        entry.models?.let {
            buffer.p1(2)
            buffer.p1(it.size)
            it.forEach(buffer::p2)
        }
        buffer.pTrue(entry.disable, 3)
        entry.recol_s.forEachIndexed { index, i ->
            if (i != 0) {
                buffer.p1(index + 40)
                buffer.p2(i)
            }
        }
        entry.recol_d.forEachIndexed { index, i ->
            if (i != 0) {
                buffer.p1(index + 50)
                buffer.p2(i)
            }
        }
        entry.headModels.forEachIndexed { index, i ->
            if (i != -1) {
                buffer.p1(index + 60)
                buffer.p2(i)
            }
        }
        buffer.p1(0)
    }
}
