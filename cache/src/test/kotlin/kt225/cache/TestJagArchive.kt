package kt225.cache

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.config.Config
import kt225.cache.inter.Interface
import kt225.cache.media.Media
import kt225.cache.models.Models
import kt225.cache.sounds.Sounds
import kt225.cache.textures.Textures
import kt225.cache.title.Title
import kt225.cache.wordenc.WordEnc
import java.nio.ByteBuffer
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * @author Jordan Abraham
 */
class TestJagArchive {
    private val injector = Guice.createInjector(CacheModule)

    @Test
    fun `test add file`() {
        val configArchive = injector.getInstance<Config>()
        val lastFiles = configArchive.keys.size

        val added = configArchive.add("test.dat", ByteBuffer.wrap(Random.nextBytes(250)))
        assertTrue(added)

        val encoded = configArchive.pack()
//        val unzipped = JagArchive.decode(encoded)
        val newConfigArchive = Config(encoded)

        val newFiles = newConfigArchive.keys.size
        assertNotEquals(lastFiles, newFiles)
        assertNotNull(newConfigArchive.read("test.dat"))
    }

    @Test
    fun `test remove file`() {
        val configArchive = injector.getInstance<Config>()
        val lastFiles = configArchive.keys.size

        val removed = configArchive.remove("varp.dat")
        assertTrue(removed)

        val encoded = configArchive.pack()
//        val unzipped = JagArchive.decode(encoded)
        val newConfigArchive = Config(encoded)

        val newFiles = newConfigArchive.keys.size
        assertNotEquals(lastFiles, newFiles)
        assertNull(newConfigArchive.read("varp.dat"))
    }

    @Test
    fun `test encode and decode config archive`() {
        val configArchive = injector.getInstance<Config>()

        val encoded = configArchive.pack()
        val newConfigArchive = Config(encoded)
        
        assertEquals(configArchive.crc, newConfigArchive.crc)
        assertTrue(configArchive.bytes.contentEquals(newConfigArchive.bytes))
    }

    @Test
    fun `test encode and decode interface archive`() {
        val interfaceArchive = injector.getInstance<Interface>()

        val encoded = interfaceArchive.pack()
        val newConfigArchive = Interface(encoded)

        assertEquals(interfaceArchive.crc, newConfigArchive.crc)
        assertTrue(interfaceArchive.bytes.contentEquals(newConfigArchive.bytes))
    }

    @Test
    fun `test encode and decode media archive`() {
        val mediaArchive = injector.getInstance<Media>()

        val encoded = mediaArchive.pack()
        val newConfigArchive = Media(encoded)

        assertEquals(mediaArchive.crc, newConfigArchive.crc)
        assertTrue(mediaArchive.bytes.contentEquals(newConfigArchive.bytes))
    }

    @Test
    fun `test encode and decode models archive`() {
        val modelsArchive = injector.getInstance<Models>()

        val encoded = modelsArchive.pack()
        val newConfigArchive = Models(encoded)

        assertEquals(modelsArchive.crc, newConfigArchive.crc)
        assertTrue(modelsArchive.bytes.contentEquals(newConfigArchive.bytes))
    }

    @Test
    fun `test encode and decode sounds archive`() {
        val soundsArchive = injector.getInstance<Sounds>()

        val encoded = soundsArchive.pack()
        val newConfigArchive = Sounds(encoded)

        assertEquals(soundsArchive.crc, newConfigArchive.crc)
        assertTrue(soundsArchive.bytes.contentEquals(newConfigArchive.bytes))
    }

    @Test
    fun `test encode and decode textures archive`() {
        val texturesArchive = injector.getInstance<Textures>()

        val encoded = texturesArchive.pack()
        val newConfigArchive = Textures(encoded)

        assertEquals(texturesArchive.crc, newConfigArchive.crc)
        assertTrue(texturesArchive.bytes.contentEquals(newConfigArchive.bytes))
    }

    @Test
    fun `test encode and decode title archive`() {
        val titleArchive = injector.getInstance<Title>()

        val encoded = titleArchive.pack()
        val newConfigArchive = Title(encoded)

        assertEquals(titleArchive.crc, newConfigArchive.crc)
        assertTrue(titleArchive.bytes.contentEquals(newConfigArchive.bytes))
    }

    @Test
    fun `test encode and decode wordenc archive`() {
        val wordEncArchive = injector.getInstance<WordEnc>()

        val encoded = wordEncArchive.pack()
        val newConfigArchive = WordEnc(encoded)

        assertEquals(wordEncArchive.crc, newConfigArchive.crc)
        assertTrue(wordEncArchive.bytes.contentEquals(newConfigArchive.bytes))
    }
}
