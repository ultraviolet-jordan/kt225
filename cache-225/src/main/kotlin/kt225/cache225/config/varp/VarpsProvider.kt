package kt225.cache225.config.varp

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.archive.config.ConfigArchive
import kt225.cache.archive.config.varp.Varps
import kt225.common.buffer.g1
import kt225.common.buffer.g2
import kt225.common.buffer.g4
import kt225.common.buffer.gString
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.p4
import kt225.common.buffer.pString
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class VarpsProvider @Inject constructor(
    private val configArchive: ConfigArchive
) : EntryProvider<VarpEntryType, Varps<VarpEntryType>> {
    override fun get(): Varps<VarpEntryType> = Varps<VarpEntryType>().also {
        val buffer = configArchive.read("varp.dat") ?: error("varp.dat file not found.")
        repeat(buffer.g2()) { varpId ->
            it[varpId] = decode(buffer, VarpEntryType(varpId))
        }
    }

    override tailrec fun decode(buffer: ByteBuffer, entry: VarpEntryType): VarpEntryType {
        when (val opcode = buffer.g1()) {
            0 -> return entry
            1 -> entry.opcode1 = buffer.g1()
            2 -> entry.opcode2 = buffer.g1()
            3 -> entry.opcode3 = true
            4 -> entry.opcode4 = false
            5 -> entry.clientCode = buffer.g2()
            6 -> entry.opcode6 = true
            7 -> entry.opcode7 = buffer.g4()
            8 -> entry.opcode8 = true
            10 -> entry.opcode10 = buffer.gString()
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(entry: VarpEntryType): ByteBuffer {
        var allocation = 1
        if (entry.opcode1 != 0) {
            allocation += 1 + 1
        }
        if (entry.opcode2 != 0) {
            allocation += 1 + 1
        }
        if (entry.opcode3) {
            allocation += 1
        }
        if (!entry.opcode4) {
            allocation += 1
        }
        if (entry.clientCode != 0) {
            allocation += 1 + 2
        }
        if (entry.opcode6) {
            allocation += 1
        }
        if (entry.opcode7 != 0) {
            allocation += 1 + 4
        }
        if (entry.opcode8) {
            allocation += 1
        }
        val opcode10 = entry.opcode10
        if (opcode10 != null) {
            allocation += (1 + opcode10.length + 1)
        }
        val buffer = ByteBuffer.allocate(allocation)
        if (entry.opcode1 != 0) {
            buffer.p1(1)
            buffer.p1(entry.opcode1)
        }
        if (entry.opcode2 != 0) {
            buffer.p1(2)
            buffer.p1(entry.opcode2)
        }
        if (entry.opcode3) {
            buffer.p1(3)
        }
        if (!entry.opcode4) {
            buffer.p1(4)
        }
        if (entry.clientCode != 0) {
            buffer.p1(5)
            buffer.p2(entry.clientCode)
        }
        if (entry.opcode6) {
            buffer.p1(6)
        }
        if (entry.opcode7 != 0) {
            buffer.p1(7)
            buffer.p4(entry.opcode7)
        }
        if (entry.opcode8) {
            buffer.p1(8)
        }
        if (opcode10 != null) {
            buffer.p1(10)
            buffer.pString(opcode10)
        }
        buffer.p1(0)
        return buffer
    }
}
