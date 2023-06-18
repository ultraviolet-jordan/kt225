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
import kt225.common.buffer.p2
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
        val datBuffer = ByteBuffer.allocate(100_000)
        datBuffer.p2(length)
        entries.values.forEach {
            val position = datBuffer.position()
            encode(datBuffer, it)
            idxBuffer.p2(datBuffer.position() - position) // The length of the encoded bytes.
        }
        configArchive.write("obj.dat", datBuffer)
        configArchive.write("obj.idx", idxBuffer)
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
            7 -> entry.xof2d = buffer.g2().let { if (it > Short.MAX_VALUE) it - 65536 else it }
            8 -> entry.yof2d = buffer.g2().let { if (it > Short.MAX_VALUE) it - 65536 else it }
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
                val iops = entry.ops ?: arrayOfNulls(5)
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
        TODO("Not yet implemented")
    }
}
