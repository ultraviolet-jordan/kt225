@file:Suppress("DuplicatedCode", "PropertyName", "LocalVariableName")

package kt225.cache225.config.npc

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.config.Config
import kt225.cache.config.npc.Npcs
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.g2
import kt225.common.buffer.gstr
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import kt225.common.buffer.pjstr
import kt225.common.buffer.position
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class NpcsProvider @Inject constructor(
    private val config: Config
) : EntryProvider<NpcEntryType, Npcs<NpcEntryType>> {
    override fun get(): Npcs<NpcEntryType> {
        val buffer = config.read("npc.dat") ?: error("npc.dat file not found.")
        val npcs = Npcs<NpcEntryType>()
        repeat(buffer.g2) {
            npcs[it] = decode(buffer, NpcEntryType(it))
        }
        return npcs
    }

    override fun write(entries: Npcs<NpcEntryType>) {
        val idxBuffer = ByteBuffer.allocate(100_000)
        val datBuffer = ByteBuffer.allocate(250_000)
        datBuffer.p2(entries.size)
        entries.values.forEach {
            val position = datBuffer.position
            encode(datBuffer, it)
            idxBuffer.p2(datBuffer.position - position) // The length of the encoded bytes.
        }
        datBuffer.flip()
        idxBuffer.flip()
        config.add("npc.dat", datBuffer)
        config.add("npc.idx", idxBuffer)
    }

    override fun decode(buffer: ByteBuffer, entry: NpcEntryType): NpcEntryType {
        when (val opcode = buffer.g1) {
            0 -> return entry
            1 -> entry.models = IntArray(buffer.g1) { buffer.g2 }
            2 -> entry.name = buffer.gstr
            3 -> entry.desc = buffer.gstr
            12 -> entry.size = buffer.g1b
            13 -> entry.readyseq = buffer.g2
            14 -> entry.walkseq = buffer.g2
            16 -> entry.disposeAlpha = true
            17 -> {
                entry.walkseq = buffer.g2
                entry.walkseq_b = buffer.g2
                entry.walkseq_r = buffer.g2
                entry.walkseq_l = buffer.g2
            }
            in 30..39 -> {
                val ops = entry.ops ?: arrayOfNulls(5)
                ops[opcode - 30] = buffer.gstr
                // Client checks for "hidden" here, but we keep it.
                entry.ops = ops
            }
            40 -> {
                val length = buffer.g1
                val recol_s = IntArray(length)
                val recol_d = IntArray(length)
                repeat(length) {
                    recol_s[it] = buffer.g2
                    recol_d[it] = buffer.g2
                }
                entry.recol_s = recol_s
                entry.recol_d = recol_d
            }
            60 -> entry.headModels = IntArray(buffer.g1) { buffer.g2 }
            90 -> entry.opcode90 = buffer.g2
            91 -> entry.opcode91 = buffer.g2
            92 -> entry.opcode92 = buffer.g2
            93 -> entry.visonmap = false
            95 -> entry.vislevel = buffer.g2
            97 -> entry.resizeh = buffer.g2
            98 -> entry.resizev = buffer.g2
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(buffer: ByteBuffer, entry: NpcEntryType) {
        buffer.pNotNull(entry.models, 1) {
            buffer.p1(it.size)
            it.forEach(buffer::p2)
        }
        buffer.pNotNull(entry.name, 2, ByteBuffer::pjstr)
        buffer.pNotNull(entry.desc, 3, ByteBuffer::pjstr)
        if (entry.size != 1) {
            buffer.p1(12)
            buffer.p1(entry.size)
        }
        buffer.pNotNegative1(entry.readyseq, 13, ByteBuffer::p2)
        if (entry.walkseq != -1 && entry.walkseq_b == -1 && entry.walkseq_r == -1 && entry.walkseq_l == -1) {
            buffer.p1(14)
            buffer.p2(entry.walkseq)
        }
        buffer.pTrue(entry.disposeAlpha, 16)
        if (entry.walkseq != -1 && entry.walkseq_b != -1 || entry.walkseq_r != -1 || entry.walkseq_l != -1) {
            buffer.p1(17)
            buffer.p2(entry.walkseq)
            buffer.p2(entry.walkseq_b)
            buffer.p2(entry.walkseq_r)
            buffer.p2(entry.walkseq_l)
        }
        entry.ops?.let {
            require(it.size <= 5)
            it.forEachIndexed { index, op ->
                buffer.pNotNull(op, index + 30, ByteBuffer::pjstr)
            }
        }
        entry.recol_s?.let {
            val length = it.size
            val recol_d = entry.recol_d
            requireNotNull(recol_d)
            require(length == recol_d.size)
            buffer.p1(40)
            buffer.p1(length)
            repeat(length) { index ->
                val cols = it[index]
                buffer.p2(cols)
                val cold = recol_d[index]
                buffer.p2(cold)
            }
        }
        buffer.pNotNull(entry.headModels, 60) {
            buffer.p1(it.size)
            it.forEach(buffer::p2)
        }
        buffer.pNotNegative1(entry.opcode90, 90, ByteBuffer::p2)
        buffer.pNotNegative1(entry.opcode91, 91, ByteBuffer::p2)
        buffer.pNotNegative1(entry.opcode92, 92, ByteBuffer::p2)
        buffer.pFalse(entry.visonmap, 93)
        buffer.pNotNegative1(entry.vislevel, 95, ByteBuffer::p2)
        if (entry.resizeh != 128) {
            buffer.p1(97)
            buffer.p2(entry.resizeh)
        }
        if (entry.resizev != 128) {
            buffer.p1(98)
            buffer.p2(entry.resizev)
        }
        buffer.p1(0)
    }
}
