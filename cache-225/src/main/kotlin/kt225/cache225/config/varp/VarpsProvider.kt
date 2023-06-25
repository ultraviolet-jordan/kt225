package kt225.cache225.config.varp

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.config.Config
import kt225.cache.config.varp.Varps
import kt225.common.buffer.g1
import kt225.common.buffer.g2
import kt225.common.buffer.g4
import kt225.common.buffer.gstr
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.p4
import kt225.common.buffer.pjstr
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class VarpsProvider @Inject constructor(
    private val config: Config
) : EntryProvider<VarpEntryType, Varps<VarpEntryType>> {
    override fun get(): Varps<VarpEntryType> {
        val buffer = config.read("varp.dat") ?: error("varp.dat file not found.")
        val varps = Varps<VarpEntryType>()
        repeat(buffer.g2()) {
            varps[it] = decode(buffer, VarpEntryType(it))
        }
        return varps
    }

    override fun write(entries: Varps<VarpEntryType>) {
        val buffer = ByteBuffer.allocate(100_000)
        buffer.p2(entries.size)
        entries.values.forEach {
            encode(buffer, it)
        }
        buffer.flip()
        config.add("varp.dat", buffer)
    }

    override tailrec fun decode(buffer: ByteBuffer, entry: VarpEntryType): VarpEntryType {
        when (val opcode = buffer.g1()) {
            0 -> return entry
            1 -> entry.opcode1 = buffer.g1()
            2 -> entry.opcode2 = buffer.g1()
            3 -> entry.opcode3 = true
            4 -> entry.opcode4 = false
            5 -> entry.clientcode = buffer.g2()
            6 -> entry.opcode6 = true
            7 -> entry.opcode7 = buffer.g4()
            8 -> entry.opcode8 = true
            10 -> entry.opcode10 = buffer.gstr()
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(buffer: ByteBuffer, entry: VarpEntryType) {
        buffer.pNotZero(entry.opcode1, 1, ByteBuffer::p1)
        buffer.pNotZero(entry.opcode2, 2, ByteBuffer::p1)
        buffer.pTrue(entry.opcode3, 3)
        buffer.pFalse(entry.opcode4, 4)
        buffer.pNotZero(entry.clientcode, 5, ByteBuffer::p2)
        buffer.pTrue(entry.opcode6, 6)
        buffer.pNotZero(entry.opcode7, 7, ByteBuffer::p4)
        buffer.pTrue(entry.opcode8, 8)
        buffer.pNotNull(entry.opcode10, 10, ByteBuffer::pjstr)
        buffer.p1(0)
    }
}
