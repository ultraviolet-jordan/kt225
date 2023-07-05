package kt225.cache225.config.npc

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.config.Config
import kt225.cache.config.npc.Npcs
import kt225.cache225.Cache225Module
import kt225.common.buffer.flip
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class TestNpcs {
    @Test
    fun `test all`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val npcs = injector.getInstance<Npcs<NpcEntryType>>()
        npcs.values.forEach(::println)
    }

    @Test
    fun `test encode decode`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val provider = injector.getInstance<NpcsProvider>()
        val npcs = injector.getInstance<Npcs<NpcEntryType>>()

        npcs.values.forEach {
            val buffer = ByteBuffer.allocate(250)
            provider.encode(buffer, it)
            val decoded = provider.decode(buffer.flip, NpcEntryType(it.id))

            assertEquals(it.id, decoded.id)
            assertContentEquals(it.models, decoded.models)
            assertEquals(it.name, decoded.name)
            assertEquals(it.desc, decoded.desc)
            assertEquals(it.size, decoded.size)
            assertEquals(it.readyseq, decoded.readyseq)
            assertEquals(it.walkseq, decoded.walkseq)
            assertEquals(it.disposeAlpha, decoded.disposeAlpha)
            assertEquals(it.walkseq_b, decoded.walkseq_b)
            assertEquals(it.walkseq_r, decoded.walkseq_r)
            assertEquals(it.walkseq_l, decoded.walkseq_l)
            assertContentEquals(it.ops, decoded.ops)
            assertContentEquals(it.recol_s, decoded.recol_s)
            assertContentEquals(it.recol_d, decoded.recol_d)
            assertContentEquals(it.headModels, decoded.headModels)
            assertEquals(it.opcode90, decoded.opcode90)
            assertEquals(it.opcode91, decoded.opcode91)
            assertEquals(it.opcode92, decoded.opcode92)
            assertEquals(it.visonmap, decoded.visonmap)
            assertEquals(it.vislevel, decoded.vislevel)
            assertEquals(it.resizeh, decoded.resizeh)
            assertEquals(it.resizev, decoded.resizev)
            assertEquals(it.hashCode(), decoded.hashCode())
        }
    }

    @Test
    fun `test rewrite`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val config = injector.getInstance<Config>()
        val provider = injector.getInstance<NpcsProvider>()
        val npcs = injector.getInstance<Npcs<NpcEntryType>>()

        provider.write(npcs)

        val edited = config.pack()
        val editedConfig = Config(edited)
        val editedProvider = NpcsProvider(editedConfig)
        val editedNpcs = editedProvider.get()

        editedNpcs.values.forEach {
            val original = npcs[it.id]
            assertEquals(original?.id, it.id)
            assertContentEquals(original?.models, it.models)
            assertEquals(original?.name, it.name)
            assertEquals(original?.desc, it.desc)
            assertEquals(original?.size, it.size)
            assertEquals(original?.readyseq, it.readyseq)
            assertEquals(original?.walkseq, it.walkseq)
            assertEquals(original?.disposeAlpha, it.disposeAlpha)
            assertEquals(original?.walkseq_b, it.walkseq_b)
            assertEquals(original?.walkseq_r, it.walkseq_r)
            assertEquals(original?.walkseq_l, it.walkseq_l)
            assertContentEquals(original?.ops, it.ops)
            assertContentEquals(original?.recol_s, it.recol_s)
            assertContentEquals(original?.recol_d, it.recol_d)
            assertContentEquals(original?.headModels, it.headModels)
            assertEquals(original?.opcode90, it.opcode90)
            assertEquals(original?.opcode91, it.opcode91)
            assertEquals(original?.opcode92, it.opcode92)
            assertEquals(original?.visonmap, it.visonmap)
            assertEquals(original?.vislevel, it.vislevel)
            assertEquals(original?.resizeh, it.resizeh)
            assertEquals(original?.resizev, it.resizev)
            assertEquals(original.hashCode(), it.hashCode())
        }
    }
}
