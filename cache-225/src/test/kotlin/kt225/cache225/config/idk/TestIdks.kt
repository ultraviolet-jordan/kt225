package kt225.cache225.config.idk

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.config.Config
import kt225.cache.config.idk.Idks
import kt225.cache225.Cache225Module
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class TestIdks {
    @Test
    fun `test all`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val idks = injector.getInstance<Idks<IdkEntryType>>()
        idks.values.forEach(::println)
    }

    @Test
    fun `test encode decode`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val provider = injector.getInstance<IdksProvider>()
        val idks = injector.getInstance<Idks<IdkEntryType>>()

        idks.values.forEach {
            val buffer = ByteBuffer.allocate(250)
            provider.encode(buffer, it)
            buffer.flip()
            val decoded = provider.decode(buffer, IdkEntryType(it.id))

            assertEquals(it.id, decoded.id)
            assertEquals(it.type, decoded.type)
            assertContentEquals(it.models, decoded.models)
            assertEquals(it.disable, decoded.disable)
            assertContentEquals(it.recol_s, decoded.recol_s)
            assertContentEquals(it.recol_d, decoded.recol_d)
            assertContentEquals(it.headModels, decoded.headModels)
            assertEquals(it.hashCode(), decoded.hashCode())
        }
    }

    @Test
    fun `test rewrite`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val config = injector.getInstance<Config>()
        val provider = injector.getInstance<IdksProvider>()
        val idks = injector.getInstance<Idks<IdkEntryType>>()

        provider.write(idks)

        val edited = config.pack()
        val editedConfig = Config(edited)
        val editedProvider = IdksProvider(editedConfig)
        val editedFlos = editedProvider.read()
        
        editedFlos.values.forEach {
            val original = idks[it.id]
            assertEquals(original?.id, it.id)
            assertEquals(original?.type, it.type)
            assertContentEquals(original?.models, it.models)
            assertEquals(original?.disable, it.disable)
            assertContentEquals(original?.recol_s, it.recol_s)
            assertContentEquals(original?.recol_d, it.recol_d)
            assertContentEquals(original?.headModels, it.headModels)
            assertEquals(original.hashCode(), it.hashCode())
        }
    }
}
