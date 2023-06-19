package kt225.cache225.config.obj

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.archive.config.ConfigArchive
import kt225.cache.archive.config.obj.Objs
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
    private val configArchive: ConfigArchive
) : EntryProvider<ObjEntryType, Objs<ObjEntryType>> {
    override fun read(): Objs<ObjEntryType> {
        val buffer = configArchive.read("obj.dat") ?: error("obj.dat file not found.")
        val objs = Objs<ObjEntryType>()
        repeat(buffer.g2()) {
            objs[it] = decode(buffer, ObjEntryType(it))
        }
        return objs
    }

    override fun write(entries: Objs<ObjEntryType>) {
        val length = entries.size
        val idxBuffer = ByteBuffer.allocate(100_000)
        val datBuffer = ByteBuffer.allocate(250_000)
        datBuffer.p2(length)
        entries.values.forEach {
            val position = datBuffer.position()
            encode(datBuffer, it)
            idxBuffer.p2(datBuffer.position() - position) // The length of the encoded bytes.
        }
        datBuffer.flip()
        idxBuffer.flip()
        configArchive.add("obj.dat", datBuffer)
        configArchive.add("obj.idx", idxBuffer)
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
        if (entry.model != 0) {
            buffer.p1(1)
            require(entry.model in -65535..65535)
            buffer.p2(entry.model)
        }
        entry.name?.let {
            buffer.p1(2)
            buffer.pjstr(it)
        }
        entry.desc?.let {
            buffer.p1(3)
            buffer.pjstr(it)
        }
        if (entry.zoom2d != 2000) {
            buffer.p1(4)
            require(entry.zoom2d in -65535..65535)
            buffer.p2(entry.zoom2d)
        }
        if (entry.xan2d != 0) {
            buffer.p1(5)
            require(entry.xan2d in -65535..65535)
            buffer.p2(entry.xan2d)
        }
        if (entry.yan2d != 0) {
            buffer.p1(6)
            require(entry.yan2d in -65535..65535)
            buffer.p2(entry.yan2d)
        }
        if (entry.xof2d != 0) {
            buffer.p1(7)
            require(entry.xof2d in -65535..65535)
            buffer.p2(entry.xof2d)
        }
        if (entry.yof2d != 0) {
            buffer.p1(8)
            require(entry.yof2d in -65535..65535)
            buffer.p2(entry.yof2d)
        }
        if (entry.opcode9) {
            buffer.p1(9)
        }
        if (entry.opcode10 != -1) {
            buffer.p1(10)
            require(entry.opcode10 in -65535..65535)
            buffer.p2(entry.opcode10)
        }
        if (entry.stackable) {
            buffer.p1(11)
        }
        if (entry.cost != 1) {
            buffer.p1(12)
            require(entry.manwear in Int.MIN_VALUE..Int.MAX_VALUE)
            buffer.p4(entry.cost)
        }
        if (entry.members) {
            buffer.p1(16)
        }
        if (entry.manwear != -1) {
            buffer.p1(23)
            require(entry.manwear in -65535..65535)
            buffer.p2(entry.manwear)
            require(entry.manwearOffsetY in -255..255)
            buffer.p1(entry.manwearOffsetY)
        }
        if (entry.manwear2 != -1) {
            buffer.p1(24)
            require(entry.manwear2 in -65535..65535)
            buffer.p2(entry.manwear2)
        }
        if (entry.womanwear != -1) {
            buffer.p1(25)
            require(entry.womanwear in -65535..65535)
            buffer.p2(entry.womanwear)
            require(entry.womanwearOffsetY in -255..255)
            buffer.p1(entry.womanwearOffsetY)
        }
        if (entry.womanwear2 != -1) {
            buffer.p1(26)
            require(entry.womanwear2 in -65535..65535)
            buffer.p2(entry.womanwear2)
        }
        entry.ops?.let {
            require(it.size <= 5)
            it.forEachIndexed { index, op ->
                if (op == null) {
                    return@forEachIndexed
                }
                buffer.p1(index + 30)
                buffer.pjstr(op)
            }
        }
        entry.iops?.let {
            require(it.size <= 5)
            it.forEachIndexed { index, op ->
                if (op == null) {
                    return@forEachIndexed
                }
                buffer.p1(index + 35)
                buffer.pjstr(op)
            }
        }
        entry.recol_s?.let {
            val length = it.size
            val recol_d = entry.recol_d
            requireNotNull(recol_d)
            require(length == recol_d.size)
            buffer.p1(40)
            require(length in -255..255)
            buffer.p1(length)
            repeat(length) { index ->
                val cols = it[index]
                require(cols in -65535..65535)
                buffer.p2(cols)
                val cold = recol_d[index]
                require(cold in -65535..65535)
                buffer.p2(cold)
            }
        }
        if (entry.manwear3 != -1) {
            buffer.p1(78)
            require(entry.manwear3 in -65535..65535)
            buffer.p2(entry.manwear3)
        }
        if (entry.womanwear3 != -1) {
            buffer.p1(79)
            require(entry.womanwear3 in -65535..65535)
            buffer.p2(entry.womanwear3)
        }
        if (entry.manhead != -1) {
            buffer.p1(90)
            require(entry.manhead in -65535..65535)
            buffer.p2(entry.manhead)
        }
        if (entry.womanhead != -1) {
            buffer.p1(91)
            require(entry.womanhead in -65535..65535)
            buffer.p2(entry.womanhead)
        }
        if (entry.manhead2 != -1) {
            buffer.p1(92)
            require(entry.manhead2 in -65535..65535)
            buffer.p2(entry.manhead2)
        }
        if (entry.womanhead2 != -1) {
            buffer.p1(93)
            require(entry.womanhead2 in -65535..65535)
            buffer.p2(entry.womanhead2)
        }
        if (entry.zan2d != 0) {
            buffer.p1(95)
            require(entry.zan2d in -65535..65535)
            buffer.p2(entry.zan2d)
        }
        if (entry.certlink != -1) {
            buffer.p1(97)
            require(entry.certlink in -65535..65535)
            buffer.p2(entry.certlink)
        }
        if (entry.certtemplate != -1) {
            buffer.p1(98)
            require(entry.certtemplate in -65535..65535)
            buffer.p2(entry.certtemplate)
        }
        entry.countobj?.let {
            val length = it.size
            val countco = entry.countco
            requireNotNull(countco)
            require(length == countco.size)
            repeat(length) { index ->
                buffer.p1(index + 100)
                val obj = it[index]
                require(obj in -65535..65535)
                buffer.p2(obj)
                val co = countco[index]
                require(co in -65535..65535)
                buffer.p2(co)
            }
        }
        buffer.p1(0)
    }
}
