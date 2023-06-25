package kt225.cache225.config.flo

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.config.Config
import kt225.cache.config.flo.Flos
import kt225.cache225.Cache225Module
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class TestFlos {
    @Test
    fun `test flos`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val flos = injector.getInstance<Flos<FloEntryType>>()
        flos.values.forEach(::println)
    }
    
    @Test
    fun `test encode decode`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val provider = injector.getInstance<FlosProvider>()
        val flos = injector.getInstance<Flos<FloEntryType>>()
        
        flos.values.forEach {
            val buffer = ByteBuffer.allocate(250)
            provider.encode(buffer, it)
            buffer.flip()
            val decoded = provider.decode(buffer, FloEntryType(it.id))
            
            assertEquals(it.id, decoded.id)
            assertEquals(it.rgb, decoded.rgb)
            assertEquals(it.texture, decoded.texture)
            assertEquals(it.opcode3, decoded.opcode3)
            assertEquals(it.occlude, decoded.occlude)
            assertEquals(it.name, decoded.name)
            assertEquals(it.hashCode(), decoded.hashCode())
        }
    }

    @Test
    fun `test rewrite`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val config = injector.getInstance<Config>()
        val provider = injector.getInstance<FlosProvider>()
        val flos = injector.getInstance<Flos<FloEntryType>>()
        
        provider.write(flos)
        
        val edited = config.pack()
        val editedConfig = Config(edited)
        val editedProvider = FlosProvider(editedConfig)
        val editedFlos = editedProvider.read()
        
        editedFlos.values.forEach {
            val original = flos[it.id]
            assertEquals(original?.id, it.id)
            assertEquals(original?.rgb, it.rgb)
            assertEquals(original?.texture, it.texture)
            assertEquals(original?.opcode3, it.opcode3)
            assertEquals(original?.occlude, it.occlude)
            assertEquals(original?.name, it.name)
            assertEquals(original.hashCode(), it.hashCode())
        }
    }
}
