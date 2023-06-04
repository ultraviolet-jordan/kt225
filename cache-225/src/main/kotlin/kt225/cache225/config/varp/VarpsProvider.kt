package kt225.cache225.config.varp

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.archive.EntryProvider
import kt225.cache.archive.config.ConfigArchive
import kt225.cache.archive.config.varp.Varps
import kt225.common.buffer.RSByteBuffer
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class VarpsProvider @Inject constructor(
    private val configArchive: ConfigArchive
) : EntryProvider<VarpEntryType, Varps<VarpEntryType>> {

    override tailrec fun decode(buffer: RSByteBuffer, type: VarpEntryType): VarpEntryType {
        when (val opcode = buffer.g1()) {
            0 -> return type
            1 -> type.opcode1 = buffer.g1()
            2 -> type.opcode2 = buffer.g1()
            3 -> type.opcode3 = true
            4 -> type.opcode4 = false
            5 -> type.clientCode = buffer.g2()
            6 -> type.opcode6 = true
            7 -> type.opcode7 = buffer.g4()
            8 -> type.opcode8 = true
            10 -> type.opcode10 = buffer.gstr()
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, type)
    }

    override fun encode(type: VarpEntryType): RSByteBuffer {
        var allocation = 1
        if (type.opcode1 != 0) {
            allocation += 1 + 1
        }
        if (type.opcode2 != 0) {
            allocation += 1 + 1
        }
        if (type.opcode3) {
            allocation += 1
        }
        if (!type.opcode4) {
            allocation += 1
        }
        if (type.clientCode != 0) {
            allocation += 1 + 2
        }
        if (type.opcode6) {
            allocation += 1
        }
        if (type.opcode7 != 0) {
            allocation += 1 + 4
        }
        if (type.opcode8) {
            allocation += 1
        }
        val opcode10 = type.opcode10
        if (opcode10 != null) {
            allocation += (1 + opcode10.length + 1)
        }
        val buffer = RSByteBuffer(ByteBuffer.allocate(allocation))
        if (type.opcode1 != 0) {
            buffer.p1(1)
            buffer.p1(type.opcode1)
        }
        if (type.opcode2 != 0) {
            buffer.p1(2)
            buffer.p1(type.opcode2)
        }
        if (type.opcode3) {
            buffer.p1(3)
        }
        if (!type.opcode4) {
            buffer.p1(4)
        }
        if (type.clientCode != 0) {
            buffer.p1(5)
            buffer.p2(type.clientCode)
        }
        if (type.opcode6) {
            buffer.p1(6)
        }
        if (type.opcode7 != 0) {
            buffer.p1(7)
            buffer.p4(type.opcode7)
        }
        if (type.opcode8) {
            buffer.p1(8)
        }
        if (opcode10 != null) {
            buffer.p1(10)
            buffer.pjstr(opcode10)
        }
        buffer.p1(0)
        return buffer
    }

    override fun get(): Varps<VarpEntryType> = Varps<VarpEntryType>().also {
        val buffer = configArchive.file("varp.dat")?.buffer() ?: error("varp.dat file not found.")
        repeat(buffer.g2()) { varpId ->
            it[varpId] = decode(buffer, VarpEntryType(varpId))
        }
    }
}
