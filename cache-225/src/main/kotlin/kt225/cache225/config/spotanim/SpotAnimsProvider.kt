@file:Suppress("LocalVariableName")

package kt225.cache225.config.spotanim

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.config.Config
import kt225.cache.config.spotanim.SpotAnims
import kt225.common.buffer.flip
import kt225.common.buffer.g1
import kt225.common.buffer.g2
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class SpotAnimsProvider @Inject constructor(
    private val config: Config
) : EntryProvider<SpotAnimEntryType, SpotAnims<SpotAnimEntryType>> {
    override fun get(): SpotAnims<SpotAnimEntryType> {
        val buffer = config.read("spotanim.dat") ?: error("spotanim.dat file not found.")
        val spotAnims = SpotAnims<SpotAnimEntryType>()
        repeat(buffer.g2) {
            spotAnims[it] = decode(buffer, SpotAnimEntryType(it))
        }
        return spotAnims
    }

    override fun write(entries: SpotAnims<SpotAnimEntryType>) {
        val buffer = ByteBuffer.allocate(100_000)
        buffer.p2(entries.size)
        entries.values.forEach {
            encode(buffer, it)
        }
        config.add("spotanim.dat", buffer.flip)
    }

    override tailrec fun decode(buffer: ByteBuffer, entry: SpotAnimEntryType): SpotAnimEntryType {
        when (val opcode = buffer.g1) {
            0 -> return entry
            1 -> entry.model = buffer.g2
            2 -> entry.anim = buffer.g2
            3 -> entry.disposeAlpha = true
            4 -> entry.resizeh = buffer.g2
            5 -> entry.resizev = buffer.g2
            6 -> entry.rotation = buffer.g2
            7 -> entry.ambient = buffer.g1
            8 -> entry.contrast = buffer.g1
            in 40..49 -> entry.recol_s[opcode - 40] = buffer.g2
            in 50..59 -> entry.recol_d[opcode - 50] = buffer.g2
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(buffer: ByteBuffer, entry: SpotAnimEntryType) {
        buffer.pNotZero(entry.model, 1, ByteBuffer::p2)
        buffer.pNotNegative1(entry.anim, 2, ByteBuffer::p2)
        buffer.pTrue(entry.disposeAlpha, 3)
        if (entry.resizeh != 128) {
            buffer.p1(4)
            buffer.p2(entry.resizeh)
        }
        if (entry.resizev != 128) {
            buffer.p1(5)
            buffer.p2(entry.resizev)
        }
        buffer.pNotZero(entry.rotation, 6, ByteBuffer::p2)
        buffer.pNotZero(entry.ambient, 7, ByteBuffer::p1)
        buffer.pNotZero(entry.contrast, 8, ByteBuffer::p1)
        entry.recol_s.forEachIndexed { index, recol_s ->
            buffer.pNotZero(recol_s, index + 40, ByteBuffer::p2)
        }
        entry.recol_d.forEachIndexed { index, recol_d ->
            buffer.pNotZero(recol_d, index + 50, ByteBuffer::p2)
        }
        buffer.p1(0)
    }
}
