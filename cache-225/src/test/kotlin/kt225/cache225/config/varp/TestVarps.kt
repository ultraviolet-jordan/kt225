package kt225.cache225.config.varp

import com.google.inject.Guice
import com.google.inject.Injector
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.archive.config.varp.Varps
import kt225.common.buffer.RSByteBuffer
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class TestVarps {

    @Test
    fun `test varps`() {
        val injector: Injector = Guice.createInjector(CacheModule())
        val varps = injector.getInstance<Varps<VarpEntryType>>()
        varps.values.forEach(::println)
        assert(varps.values.last().id == 294)
    }

    @Test
    fun `test encode decode`() {
        val injector: Injector = Guice.createInjector(CacheModule())
        val provider = injector.getInstance<VarpsProvider>()
        val varps = injector.getInstance<Varps<VarpEntryType>>()

        varps.values.forEach {
            val encoded = provider.encode(it).array()
            val decoded = provider.decode(RSByteBuffer(encoded), VarpEntryType(it.id))

            assertEquals(it.id, decoded.id)
            assertEquals(it.opcode1, decoded.opcode1)
            assertEquals(it.opcode2, decoded.opcode2)
            assertEquals(it.opcode3, decoded.opcode3)
            assertEquals(it.opcode4, decoded.opcode4)
            assertEquals(it.clientCode, decoded.clientCode)
            assertEquals(it.opcode6, decoded.opcode6)
            assertEquals(it.opcode7, decoded.opcode7)
            assertEquals(it.opcode8, decoded.opcode8)
            assertEquals(it.opcode10, decoded.opcode10)
        }
    }
}
