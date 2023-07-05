@file:Suppress("DuplicatedCode", "PropertyName", "LocalVariableName")

package kt225.cache225.config.loc

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.config.Config
import kt225.cache.config.loc.Locs
import kt225.common.buffer.g1
import kt225.common.buffer.g1b
import kt225.common.buffer.g2
import kt225.common.buffer.g2b
import kt225.common.buffer.gstr
import kt225.common.buffer.p2
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class LocsProvider @Inject constructor(
    private val config: Config
) : EntryProvider<LocEntryType, Locs<LocEntryType>> {
    override fun get(): Locs<LocEntryType> {
        val buffer = config.read("loc.dat") ?: error("loc.dat file not found.")
        val locs = Locs<LocEntryType>()
        repeat(buffer.g2) {
            locs[it] = decode(buffer, LocEntryType(it))
        }
        return locs
    }
    
    override fun write(entries: Locs<LocEntryType>) {
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
        config.add("loc.dat", datBuffer)
        config.add("loc.idx", idxBuffer)
    }

    override tailrec fun decode(buffer: ByteBuffer, entry: LocEntryType): LocEntryType {
        when (val opcode = buffer.g1) {
            0 -> return entry
            1 -> {
                val length = buffer.g1
                val models = IntArray(length)
                val shapes = IntArray(length)
                repeat(length) {
                    models[it] = buffer.g2
                    shapes[it] = buffer.g1
                }
                entry.models = models
                entry.shapes = shapes
            }
            2 -> entry.name = buffer.gstr
            3 -> entry.desc = buffer.gstr
            14 -> entry.width = buffer.g1
            15 -> entry.length = buffer.g1
            17 -> entry.blockwalk = false
            18 -> entry.blockproj = false
            19 -> {
                val interactive = buffer.g1
                if (interactive == 1) {
                    entry.intractable = true
                }
                entry.interactive = interactive
            }
            21 -> entry.hillskew = true
            22 -> entry.computeVertexColors = true
            23 -> entry.occlude = true
            24 -> entry.seq = buffer.g2
            25 -> entry.disposeAlpha = true
            28 -> entry.walloff = buffer.g1
            29 -> entry.ambient = buffer.g1b
            in 30..34 -> {
                val ops = entry.ops ?: arrayOfNulls(5)
                ops[opcode - 30] = buffer.gstr
                // Client checks for "hidden" here, but we keep it.
                entry.ops = ops
            }
            39 -> entry.contrast = buffer.g1b
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
            60 -> entry.mapfunction = buffer.g2
            62 -> entry.mirror = true
            64 -> entry.active = false
            65 -> entry.resizex = buffer.g2
            66 -> entry.resizey = buffer.g2
            67 -> entry.resizez = buffer.g2
            68 -> entry.mapscene = buffer.g2
            69 -> entry.blocksides = buffer.g1
            70 -> entry.xoff = buffer.g2b
            71 -> entry.yoff = buffer.g2b
            72 -> entry.zoff = buffer.g2b
            73 -> entry.forcedecor = true
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(buffer: ByteBuffer, entry: LocEntryType) {
        TODO("Not yet implemented")
    }
}
