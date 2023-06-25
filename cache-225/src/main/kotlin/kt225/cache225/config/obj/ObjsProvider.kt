package kt225.cache225.config.obj

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.config.Config
import kt225.cache.config.obj.Objs
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
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
class ObjsProvider @Inject constructor(
    private val config: Config
) : EntryProvider<ObjEntryType, Objs<ObjEntryType>> {
    override fun get(): Objs<ObjEntryType> {
        val buffer = config.read("obj.dat") ?: error("obj.dat file not found.")
        val objs = Objs<ObjEntryType>()
        repeat(buffer.g2()) {
            objs[it] = decode(buffer, ObjEntryType(it))
        }
        return objs
    }

    override fun write(entries: Objs<ObjEntryType>) {
        val idxBuffer = ByteBuffer.allocate(100_000)
        val datBuffer = ByteBuffer.allocate(250_000)
        datBuffer.p2(entries.size)
        entries.values.forEach {
            val position = datBuffer.position()
            encode(datBuffer, it)
            idxBuffer.p2(datBuffer.position() - position) // The length of the encoded bytes.
        }
        datBuffer.flip()
        idxBuffer.flip()
        config.add("obj.dat", datBuffer)
        config.add("obj.idx", idxBuffer)
    }

    override tailrec fun decode(buffer: ByteBuffer, entry: ObjEntryType): ObjEntryType {
        when (val opcode = buffer.g1()) {
            0 -> return entry
            1 -> entry.model = buffer.g2()
            2 -> entry.name = buffer.gstr()
            3 -> entry.desc = buffer.gstr()
            4 -> entry.zoom2d = buffer.g2()
            5 -> entry.xan2d = buffer.g2()
            6 -> entry.yan2d = buffer.g2()
            7 -> entry.xof2d = buffer.g2()
            8 -> entry.yof2d = buffer.g2()
            9 -> entry.opcode9 = true
            10 -> entry.opcode10 = buffer.g2()
            11 -> entry.stackable = true
            12 -> entry.cost = buffer.g4()
            16 -> entry.members = true
            23 -> {
                entry.manwear = buffer.g2()
                entry.manwearOffsetY = buffer.g1b()
            }
            24 -> entry.manwear2 = buffer.g2()
            25 -> {
                entry.womanwear = buffer.g2()
                entry.womanwearOffsetY = buffer.g1b()
            }
            26 -> entry.womanwear2 = buffer.g2()
            in 30..34 -> {
                val ops = entry.ops ?: arrayOfNulls(5)
                ops[opcode - 30] = buffer.gstr()
                // Client checks for "hidden" here, but we keep it.
                entry.ops = ops
            }
            in 35..39 -> {
                val iops = entry.iops ?: arrayOfNulls(5)
                iops[opcode - 35] = buffer.gstr()
                entry.iops = iops
            }
            40 -> {
                val length = buffer.g1()
                val recol_s = IntArray(length)
                val recol_d = IntArray(length)
                repeat(length) {
                    recol_s[it] = buffer.g2()
                    recol_d[it] = buffer.g2()
                }
                entry.recol_s = recol_s
                entry.recol_d = recol_d
            }
            78 -> entry.manwear3 = buffer.g2()
            79 -> entry.womanwear3 = buffer.g2()
            90 -> entry.manhead = buffer.g2()
            91 -> entry.womanhead = buffer.g2()
            92 -> entry.manhead2 = buffer.g2()
            93 -> entry.womanhead2 = buffer.g2()
            95 -> entry.zan2d = buffer.g2()
            97 -> entry.certlink = buffer.g2()
            98 -> entry.certtemplate = buffer.g2()
            in 100..109 -> {
                val countobj = entry.countobj ?: IntArray(10)
                val countco = entry.countco ?: IntArray(10)
                countobj[opcode - 100] = buffer.g2()
                countco[opcode - 100] = buffer.g2()
                entry.countobj = countobj
                entry.countco = countco
            }
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(buffer: ByteBuffer, entry: ObjEntryType) {
        buffer.pNotZero(entry.model, 1, ByteBuffer::p2)
        buffer.pNotNull(entry.name, 2, ByteBuffer::pjstr)
        buffer.pNotNull(entry.desc, 3, ByteBuffer::pjstr)
        if (entry.zoom2d != 2000) {
            buffer.p1(4)
            buffer.p2(entry.zoom2d)
        }
        buffer.pNotZero(entry.xan2d, 5, ByteBuffer::p2)
        buffer.pNotZero(entry.yan2d, 6, ByteBuffer::p2)
        buffer.pNotZero(entry.xof2d, 7, ByteBuffer::p2)
        buffer.pNotZero(entry.yof2d, 8, ByteBuffer::p2)
        buffer.pTrue(entry.opcode9, 9)
        buffer.pNotNegative1(entry.opcode10, 10, ByteBuffer::p2)
        buffer.pTrue(entry.stackable, 11)
        if (entry.cost != 1) {
            buffer.p1(12)
            buffer.p4(entry.cost)
        }
        buffer.pTrue(entry.members, 16)
        buffer.pNotNegative1(entry.manwear, 23) {
            p2(it)
            p1(entry.manwearOffsetY)
        }
        buffer.pNotNegative1(entry.manwear2, 24, ByteBuffer::p2)
        buffer.pNotNegative1(entry.womanwear, 25) {
            p2(it)
            p1(entry.womanwearOffsetY)
        }
        buffer.pNotNegative1(entry.womanwear2, 26, ByteBuffer::p2)
        entry.ops?.let {
            require(it.size <= 5)
            it.forEachIndexed { index, op ->
                buffer.pNotNull(op, index + 30, ByteBuffer::pjstr)
            }
        }
        entry.iops?.let {
            require(it.size <= 5)
            it.forEachIndexed { index, op ->
                buffer.pNotNull(op, index + 35, ByteBuffer::pjstr)
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
        buffer.pNotNegative1(entry.manwear3, 78, ByteBuffer::p2)
        buffer.pNotNegative1(entry.womanwear3, 79, ByteBuffer::p2)
        buffer.pNotNegative1(entry.manhead, 90, ByteBuffer::p2)
        buffer.pNotNegative1(entry.womanhead, 91, ByteBuffer::p2)
        buffer.pNotNegative1(entry.manhead2, 92, ByteBuffer::p2)
        buffer.pNotNegative1(entry.womanhead2, 93, ByteBuffer::p2)
        buffer.pNotZero(entry.zan2d, 95, ByteBuffer::p2)
        buffer.pNotNegative1(entry.certlink, 97, ByteBuffer::p2)
        buffer.pNotNegative1(entry.certtemplate, 98, ByteBuffer::p2)
        entry.countobj?.let {
            val length = it.size
            val countco = entry.countco
            requireNotNull(countco)
            require(length == countco.size)
            repeat(length) { index ->
                buffer.p1(index + 100)
                val obj = it[index]
                buffer.p2(obj)
                val co = countco[index]
                buffer.p2(co)
            }
        }
        buffer.p1(0)
    }
}
