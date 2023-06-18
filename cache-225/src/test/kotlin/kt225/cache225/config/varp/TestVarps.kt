package kt225.cache225.config.varp

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.JagArchive
import kt225.cache.archive.config.ConfigArchive
import kt225.cache.archive.config.varp.Varps
import kt225.cache225.Cache225Module
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * @author Jordan Abraham
 */
class TestVarps {

    @Test
    fun `test varps`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val varps = injector.getInstance<Varps<VarpEntryType>>()
        assert(varps.values.last().id == 294)
    }

    @Test
    fun `test encode decode`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val provider = injector.getInstance<VarpsProvider>()
        val varps = injector.getInstance<Varps<VarpEntryType>>()

        varps.values.forEach {
            val buffer = ByteBuffer.allocate(100)
            provider.encode(buffer, it)
            buffer.flip()
            val decoded = provider.decode(buffer, VarpEntryType(it.id))

            assertEquals(it.id, decoded.id)
            assertEquals(it.opcode1, decoded.opcode1)
            assertEquals(it.opcode2, decoded.opcode2)
            assertEquals(it.opcode3, decoded.opcode3)
            assertEquals(it.opcode4, decoded.opcode4)
            assertEquals(it.clientcode, decoded.clientcode)
            assertEquals(it.opcode6, decoded.opcode6)
            assertEquals(it.opcode7, decoded.opcode7)
            assertEquals(it.opcode8, decoded.opcode8)
            assertEquals(it.opcode10, decoded.opcode10)
        }
    }

    @Test
    fun `test read write`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)

        val configArchive = injector.getInstance<ConfigArchive>()
        val configArchiveEncoded = JagArchive.encode(configArchive.unzipped())
        val newConfigArchiveUnzipped = JagArchive.decode(configArchiveEncoded)

        assertEquals(configArchive.files().size, newConfigArchiveUnzipped.files.size)

        val newConfigArchive = ConfigArchive(newConfigArchiveUnzipped)
        val varpsProvider = VarpsProvider(newConfigArchive)
        val varps = varpsProvider.read()

        assert(varps.values.last().id == 294)
    }

    @Test
    fun `test edit varp`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val configArchive = injector.getInstance<ConfigArchive>()
        val varps = injector.getInstance<Varps<VarpEntryType>>()
        val varpsProvider = injector.getInstance<VarpsProvider>()

        val varp = varps[4] ?: return
        assertNotEquals(varp.clientcode, 4)
        varp.clientcode = 4

        varpsProvider.write(varps)

        val editedConfigArchiveEncoded = JagArchive.encode(configArchive.unzipped())
        val editedConfigArchiveUnzipped = JagArchive.decode(editedConfigArchiveEncoded)
        val editedConfigArchive = ConfigArchive(editedConfigArchiveUnzipped)
        val editedVarpsProviders = VarpsProvider(editedConfigArchive)

        val editedVarps = editedVarpsProviders.read()
        val editedVarp = editedVarps[4] ?: return

        assertEquals(4, editedVarp.clientcode)
    }
}
