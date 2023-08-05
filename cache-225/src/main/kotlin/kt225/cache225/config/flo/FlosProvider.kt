package kt225.cache225.config.flo

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.config.Config
import kt225.cache.config.flo.Flos
import kt225.common.buffer.flip
import kt225.common.buffer.g1
import kt225.common.buffer.g2
import kt225.common.buffer.g3
import kt225.common.buffer.gstr
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.p3
import kt225.common.buffer.pstr
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class FlosProvider @Inject constructor(
    private val config: Config
) : EntryProvider<FloEntryType, Flos<FloEntryType>> {
    override fun get(): Flos<FloEntryType> {
        val buffer = config.read("flo.dat") ?: error("flo.dat file not found.")
        val flos = Flos<FloEntryType>()
        repeat(buffer.g2) {
            flos[it] = decode(buffer, FloEntryType(it))
        }
        return flos
    }
    
    override fun write(entries: Flos<FloEntryType>) {
        val buffer = ByteBuffer.allocate(100_000)
        buffer.p2(entries.size)
        entries.values.forEach {
            encode(buffer, it)
        }
        config.add("flo.dat", buffer.flip)
    }
    
    override tailrec fun decode(buffer: ByteBuffer, entry: FloEntryType): FloEntryType {
        when (val opcode = buffer.g1) {
            0 -> return entry
            1 -> entry.rgb = buffer.g3
            2 -> entry.texture = buffer.g1
            3 -> entry.opcode3 = true
            5 -> entry.occlude = false
            6 -> entry.name = buffer.gstr
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(buffer: ByteBuffer, entry: FloEntryType) {
        buffer.pNotNull(entry.rgb, 1, ByteBuffer::p3)
        buffer.pNotNegative1(entry.texture, 2, ByteBuffer::p1)
        buffer.pTrue(entry.opcode3, 3)
        buffer.pFalse(entry.occlude, 5)
        buffer.pNotNull(entry.name, 6, ByteBuffer::pstr)
        buffer.p1(0)
    }
}
