package kt225.cache225.config.seq

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.config.Config
import kt225.cache.config.seq.Seqs
import kt225.cache225.Cache225Module
import kt225.common.buffer.flip
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class TestSeqs {
    @Test
    fun `test all`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val seqs = injector.getInstance<Seqs<SeqEntryType>>()
        seqs.values.forEach(::println)
    }

    @Test
    fun `test encode decode`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val seqs = injector.getInstance<Seqs<SeqEntryType>>()
        val provider = injector.getInstance<SeqsProvider>()

        seqs.values.forEach {
            val buffer = ByteBuffer.allocate(1000)
            provider.encode(buffer, it)
            val decoded = provider.decode(buffer.flip, SeqEntryType(it.id))

            assertEquals(it.id, decoded.id)
            assertEquals(it.framecount, decoded.framecount)
            assertContentEquals(it.primaryFrames, decoded.primaryFrames)
            assertContentEquals(it.secondaryFrames, decoded.secondaryFrames)
            assertContentEquals(it.frameDelay, decoded.frameDelay)
            assertEquals(it.replayOff, decoded.replayOff)
            assertContentEquals(it.labelGroups, decoded.labelGroups)
            assertEquals(it.stretches, decoded.stretches)
            assertEquals(it.priority, decoded.priority)
            assertEquals(it.mainhand, decoded.mainhand)
            assertEquals(it.offhand, decoded.offhand)
            assertEquals(it.replayCount, decoded.replayCount)
            assertEquals(it.hashCode(), decoded.hashCode())
        }
    }

    @Test
    fun `test rewrite`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val seqs = injector.getInstance<Seqs<SeqEntryType>>()
        val provider = injector.getInstance<SeqsProvider>()
        val config = injector.getInstance<Config>()

        provider.write(seqs)

        val edited = config.pack()
        val editedConfig = Config(edited)
        val editedProvider = SeqsProvider(editedConfig)
        val editedSeqs = editedProvider.get()
        
        editedSeqs.values.forEach {
            val original = seqs[it.id]
            assertEquals(original?.id, it.id)
            assertEquals(original?.framecount, it.framecount)
            assertContentEquals(original?.primaryFrames, it.primaryFrames)
            assertContentEquals(original?.secondaryFrames, it.secondaryFrames)
            assertContentEquals(original?.frameDelay, it.frameDelay)
            assertEquals(original?.replayOff, it.replayOff)
            assertContentEquals(original?.labelGroups, it.labelGroups)
            assertEquals(original?.stretches, it.stretches)
            assertEquals(original?.priority, it.priority)
            assertEquals(original?.mainhand, it.mainhand)
            assertEquals(original?.offhand, it.offhand)
            assertEquals(original?.replayCount, it.replayCount)
            assertEquals(original.hashCode(), it.hashCode())
        }
    }
}
