package kt225.cache225.config.seq

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.config.Config
import kt225.cache.config.seq.Seqs
import kt225.common.buffer.g1
import kt225.common.buffer.g2
import kt225.common.buffer.p1
import kt225.common.buffer.p2
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
@Singleton
class SeqsProvider @Inject constructor(
    private val config: Config
) : EntryProvider<SeqEntryType, Seqs<SeqEntryType>> {
    override fun get(): Seqs<SeqEntryType> {
        val buffer = config.read("seq.dat") ?: error("seq.dat file not found.")
        val idk = Seqs<SeqEntryType>()
        repeat(buffer.g2()) {
            idk[it] = decode(buffer, SeqEntryType(it))
        }
        return idk
    }
    
    override fun write(entries: Seqs<SeqEntryType>) {
        val buffer = ByteBuffer.allocate(100_000)
        buffer.p2(entries.size)
        entries.values.forEach {
            encode(buffer, it)
        }
        buffer.flip()
        config.add("seq.dat", buffer)
    }
    
    override tailrec fun decode(buffer: ByteBuffer, entry: SeqEntryType): SeqEntryType {
        when (val opcode = buffer.g1()) {
            0 -> return entry
            1 -> {
                val frameCount = buffer.g1()
                val primaryFrames = IntArray(frameCount)
                val secondaryFrames = IntArray(frameCount)
                val frameDelay = IntArray(frameCount)
                repeat(frameCount) {
                    primaryFrames[it] = buffer.g2()
                    secondaryFrames[it] = buffer.g2()
                    frameDelay[it] = buffer.g2()
                }
                entry.framecount = frameCount
                entry.primaryFrames = primaryFrames
                entry.secondaryFrames = secondaryFrames
                entry.frameDelay = frameDelay
            }
            2 -> entry.replayOff = buffer.g2()
            3 -> {
                val length = buffer.g1()
                entry.labelGroups = IntArray(length + 1) {
                    if (it != length) buffer.g1() else 9999999
                }
            }
            4 -> entry.stretches = true
            5 -> entry.priority = buffer.g1()
            6 -> entry.mainhand = buffer.g2()
            7 -> entry.offhand = buffer.g2()
            8 -> entry.replayCount = buffer.g1()
            else -> error("Missing opcode $opcode.")
        }
        return decode(buffer, entry)
    }

    override fun encode(buffer: ByteBuffer, entry: SeqEntryType) {
        buffer.pNotNull(entry.primaryFrames, 1) {
            val primaryFrames = it
            val secondaryFrames = entry.secondaryFrames
            val frameDelay = entry.frameDelay
            requireNotNull(secondaryFrames)
            requireNotNull(frameDelay)
            require(primaryFrames.size == secondaryFrames.size)
            require(primaryFrames.size == frameDelay.size)
            p1(primaryFrames.size)
            repeat(primaryFrames.size) { index ->
                p2(primaryFrames[index])
                p2(secondaryFrames[index])
                p2(frameDelay[index])
            }
        }
        buffer.pNotNegative1(entry.replayOff, 2, ByteBuffer::p2)
        buffer.pNotNull(entry.labelGroups, 3) {
            p1(it.size - 1)
            repeat(it.size - 1) { index ->
                p1(it[index])
            }
        }
        buffer.pTrue(entry.stretches, 4)
        if (entry.priority != 5) {
            buffer.p1(5)
            buffer.p1(entry.priority)
        }
        buffer.pNotNegative1(entry.mainhand, 6, ByteBuffer::p2)
        buffer.pNotNegative1(entry.offhand, 7, ByteBuffer::p2)
        if (entry.replayCount != 99) {
            buffer.p1(8)
            buffer.p1(entry.replayCount)
        }
        buffer.p1(0)
    }
}
