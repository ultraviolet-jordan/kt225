package kt225.cache225.config.obj

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.config.Config
import kt225.cache.config.obj.Objs
import kt225.cache225.Cache225Module
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

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

    @Test
    fun `test encode decode`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val provider = injector.getInstance<ObjsProvider>()
        val varps = injector.getInstance<Objs<ObjEntryType>>()

        varps.values.forEach {
            val buffer = ByteBuffer.allocate(250)
            provider.encode(buffer, it)
            buffer.flip()
            val decoded = provider.decode(buffer, ObjEntryType(it.id))

            assertEquals(it.id, decoded.id)
            assertEquals(it.model, decoded.model)
            assertEquals(it.name, decoded.name)
            assertEquals(it.desc, decoded.desc)
            assertEquals(it.zoom2d, decoded.zoom2d)
            assertEquals(it.xan2d, decoded.xan2d)
            assertEquals(it.yan2d, decoded.yan2d)
            assertEquals(it.zan2d, decoded.zan2d)
            assertEquals(it.xof2d, decoded.xof2d)
            assertEquals(it.yof2d, decoded.yof2d)
            assertEquals(it.opcode9, decoded.opcode9)
            assertEquals(it.opcode10, decoded.opcode10)
            assertEquals(it.stackable, decoded.stackable)
            assertEquals(it.cost, decoded.cost)
            assertEquals(it.members, decoded.members)
            assertEquals(it.manwear, decoded.manwear)
            assertEquals(it.manwear2, decoded.manwear2)
            assertEquals(it.manwearOffsetY, decoded.manwearOffsetY)
            assertEquals(it.womanwear, decoded.womanwear)
            assertEquals(it.womanwear2, decoded.womanwear2)
            assertEquals(it.womanwearOffsetY, decoded.womanwearOffsetY)
            assertEquals(it.manwear3, decoded.manwear3)
            assertEquals(it.womanwear3, decoded.womanwear3)
            assertEquals(it.manhead, decoded.manhead)
            assertEquals(it.manhead2, decoded.manhead2)
            assertEquals(it.womanhead, decoded.womanhead)
            assertEquals(it.womanhead2, decoded.womanhead2)
            assertContentEquals(it.ops, decoded.ops)
            assertContentEquals(it.iops, decoded.iops)
            assertContentEquals(it.recol_s, decoded.recol_s)
            assertContentEquals(it.recol_d, decoded.recol_d)
            assertEquals(it.certlink, decoded.certlink)
            assertEquals(it.certtemplate, decoded.certtemplate)
            assertContentEquals(it.countobj, decoded.countobj)
            assertContentEquals(it.countco, decoded.countco)
            assertEquals(it.hashCode(), decoded.hashCode())
        }
    }

    @Test
    fun `test obj rewrite`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val configArchive = injector.getInstance<Config>()
        val objs = injector.getInstance<Objs<ObjEntryType>>()
        val objsProvider = injector.getInstance<ObjsProvider>()

        objsProvider.write(objs)

        val editedConfigArchiveEncoded = configArchive.pack()
        val editedConfigArchive = Config(editedConfigArchiveEncoded)
        val editedObjsProviders = ObjsProvider(editedConfigArchive)
        val editedObjs = editedObjsProviders.read()

        editedObjs.values.forEach {
            val original = objs[it.id]
            assertEquals(original?.id, it.id)
            assertEquals(original?.model, it.model)
            assertEquals(original?.name, it.name)
            assertEquals(original?.desc, it.desc)
            assertEquals(original?.zoom2d, it.zoom2d)
            assertEquals(original?.xan2d, it.xan2d)
            assertEquals(original?.yan2d, it.yan2d)
            assertEquals(original?.zan2d, it.zan2d)
            assertEquals(original?.xof2d, it.xof2d)
            assertEquals(original?.yof2d, it.yof2d)
            assertEquals(original?.opcode9, it.opcode9)
            assertEquals(original?.opcode10, it.opcode10)
            assertEquals(original?.stackable, it.stackable)
            assertEquals(original?.cost, it.cost)
            assertEquals(original?.members, it.members)
            assertEquals(original?.manwear, it.manwear)
            assertEquals(original?.manwear2, it.manwear2)
            assertEquals(original?.manwearOffsetY, it.manwearOffsetY)
            assertEquals(original?.womanwear, it.womanwear)
            assertEquals(original?.womanwear2, it.womanwear2)
            assertEquals(original?.womanwearOffsetY, it.womanwearOffsetY)
            assertEquals(original?.manwear3, it.manwear3)
            assertEquals(original?.womanwear3, it.womanwear3)
            assertEquals(original?.manhead, it.manhead)
            assertEquals(original?.manhead2, it.manhead2)
            assertEquals(original?.womanhead, it.womanhead)
            assertEquals(original?.womanhead2, it.womanhead2)
            assertContentEquals(original?.ops, it.ops)
            assertContentEquals(original?.iops, it.iops)
            assertContentEquals(original?.recol_s, it.recol_s)
            assertContentEquals(original?.recol_d, it.recol_d)
            assertEquals(original?.certlink, it.certlink)
            assertEquals(original?.certtemplate, it.certtemplate)
            assertContentEquals(original?.countobj, it.countobj)
            assertContentEquals(original?.countco, it.countco)
            assertEquals(original?.hashCode(), it.hashCode())
        }
    }

    @Test
    fun `test edit obj`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val configArchive = injector.getInstance<Config>()
        val objs = injector.getInstance<Objs<ObjEntryType>>()
        val objsProvider = injector.getInstance<ObjsProvider>()
        
        objs.size

        val obj = objs[1333] ?: return
        obj.name = "The God Slayer"
        objsProvider.write(objs)

        val editedConfigArchiveEncoded = configArchive.pack()
        val editedConfigArchive = Config(editedConfigArchiveEncoded)
        val editedObjsProviders = ObjsProvider(editedConfigArchive)
        val editedObjs = editedObjsProviders.read()

        val editedObj = editedObjs[1333] ?: return

        assertEquals("The God Slayer", editedObj.name)
    }
}
