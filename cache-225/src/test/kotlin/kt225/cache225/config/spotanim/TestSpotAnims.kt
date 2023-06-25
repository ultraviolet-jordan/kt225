package kt225.cache225.config.spotanim

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.config.Config
import kt225.cache.config.spotanim.SpotAnims
import kt225.cache225.Cache225Module
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class TestSpotAnims {
    @Test
    fun `test all`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val spotAnims = injector.getInstance<SpotAnims<SpotAnimEntryType>>()
        spotAnims.values.forEach(::println)
    }

    @Test
    fun `test encode decode`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val spotAnims = injector.getInstance<SpotAnims<SpotAnimEntryType>>()
        val provider = injector.getInstance<SpotAnimsProvider>()
        
        spotAnims.values.forEach {
            val buffer = ByteBuffer.allocate(250)
            provider.encode(buffer, it)
            buffer.flip()
            val decoded = provider.decode(buffer, SpotAnimEntryType(it.id))

            assertEquals(it.id, decoded.id)
            assertEquals(it.model, decoded.model)
            assertEquals(it.anim, decoded.anim)
            assertEquals(it.disposeAlpha, decoded.disposeAlpha)
            assertEquals(it.resizeh, decoded.resizeh)
            assertEquals(it.resizev, decoded.resizev)
            assertEquals(it.rotation, decoded.rotation)
            assertEquals(it.ambient, decoded.ambient)
            assertEquals(it.contrast, decoded.contrast)
            assertContentEquals(it.recol_s, decoded.recol_s)
            assertContentEquals(it.recol_d, decoded.recol_d)
            assertEquals(it.hashCode(), decoded.hashCode())
        }
    }

    @Test
    fun `test rewrite`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val spotAnims = injector.getInstance<SpotAnims<SpotAnimEntryType>>()
        val provider = injector.getInstance<SpotAnimsProvider>()
        val config = injector.getInstance<Config>()

        provider.write(spotAnims)

        val edited = config.pack()
        val editedConfig = Config(edited)
        val editedProvider = SpotAnimsProvider(editedConfig)
        val editSpotAnims = editedProvider.get()

        editSpotAnims.values.forEach {
            val original = spotAnims[it.id]
            assertEquals(original?.id, it.id)
            assertEquals(original?.model, it.model)
            assertEquals(original?.anim, it.anim)
            assertEquals(original?.disposeAlpha, it.disposeAlpha)
            assertEquals(original?.resizeh, it.resizeh)
            assertEquals(original?.resizev, it.resizev)
            assertEquals(original?.rotation, it.rotation)
            assertEquals(original?.ambient, it.ambient)
            assertEquals(original?.contrast, it.contrast)
            assertContentEquals(original?.recol_s, it.recol_s)
            assertContentEquals(original?.recol_d, it.recol_d)
            assertEquals(original.hashCode(), it.hashCode())
        }
    }
}
