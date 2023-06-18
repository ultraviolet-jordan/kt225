package kt225.cache225.config.obj

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.archive.config.obj.Objs
import kt225.cache225.Cache225Module
import kotlin.test.Test

/**
 * @author Jordan Abraham
 */
class TestObjs {
    @Test
    fun `test objs`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val objs = injector.getInstance<Objs<ObjEntryType>>()
        objs.values.forEach(::println)
        assert(objs.values.last().id == 2885)
        assert(objs[1333]?.name == "Rune scimitar")
    }

    /*@Test
    fun `test edit obj`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val configArchive = injector.getInstance<ConfigArchive>()
        val objs = injector.getInstance<Objs<ObjEntryType>>()
        val objsProvider = injector.getInstance<ObjsProvider>()

        val obj = objs[1333] ?: return
        obj.name = "The Scim of Death"

        val buffer = ByteBuffer.allocate(100)
        objsProvider.encode(buffer, obj)
        buffer.flip()
        objsProvider.write(objs)

        val editedConfigArchiveEncoded = JagArchive.encode(configArchive.unzipped())
        val editedConfigArchiveDecoded = JagArchive.decode(editedConfigArchiveEncoded)
        val editedConfigArchive = ConfigArchive(editedConfigArchiveDecoded)
        val editedObjsProviders = ObjsProvider(editedConfigArchive)

        val editedObjs = editedObjsProviders.get()
        val editedObj = editedObjs[1333] ?: return

        assertEquals("The Scim of Death", editedObj.name)
    }*/
}
