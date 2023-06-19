package kt225.cache

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.archive.config.ConfigArchive
import kt225.cache.archive.inter.InterfaceArchive
import kt225.cache.archive.media.MediaArchive
import kt225.cache.archive.models.ModelsArchive
import kt225.cache.archive.sounds.SoundsArchive
import kt225.cache.archive.textures.TexturesArchive
import kt225.cache.archive.title.TitleArchive
import kt225.cache.archive.wordenc.WordEncArchive
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
        val configArchive = injector.getInstance<ConfigArchive>()
        val lastFiles = configArchive.files.size

        val added = configArchive.add("test.dat", ByteBuffer.wrap(Random.nextBytes(250)))
        assertTrue(added)

        val encoded = JagArchive.encode(configArchive)
        val unzipped = JagArchive.decode(encoded)
        val newConfigArchive = ConfigArchive(unzipped)

        val newFiles = newConfigArchive.files.size
        assertNotEquals(lastFiles, newFiles)
        assertNotNull(newConfigArchive.file("test.dat"))
    }

    @Test
    fun `test remove file`() {
        val configArchive = injector.getInstance<ConfigArchive>()
        val lastFiles = configArchive.files.size

        val removed = configArchive.remove("varp.dat")
        assertTrue(removed)

        val encoded = JagArchive.encode(configArchive)
        val unzipped = JagArchive.decode(encoded)
        val newConfigArchive = ConfigArchive(unzipped)

        val newFiles = newConfigArchive.files.size
        assertNotEquals(lastFiles, newFiles)
        assertNull(newConfigArchive.file("varp.dat"))
    }

    @Test
    fun `test encode and decode config archive`() {
        val configArchive = injector.getInstance<ConfigArchive>()

        val encoded = JagArchive.encode(configArchive)
        val unzipped = JagArchive.decode(encoded)
        val newConfigArchive = ConfigArchive(unzipped)

        newConfigArchive.files.values.forEach {
            val file = configArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
        assertEquals(configArchive.crc, newConfigArchive.crc)
        assertTrue(configArchive.zipped.contentEquals(newConfigArchive.zipped))
    }

    @Test
    fun `test encode and decode interface archive`() {
        val interfaceArchive = injector.getInstance<InterfaceArchive>()

        val encoded = JagArchive.encode(interfaceArchive)
        val unzipped = JagArchive.decode(encoded)
        val newInterfaceArchive = InterfaceArchive(unzipped)

        newInterfaceArchive.files.values.forEach {
            val file = interfaceArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
        assertEquals(interfaceArchive.crc, newInterfaceArchive.crc)
        assertTrue(interfaceArchive.zipped.contentEquals(newInterfaceArchive.zipped))
    }

    @Test
    fun `test encode and decode media archive`() {
        val mediaArchive = injector.getInstance<MediaArchive>()

        val encoded = JagArchive.encode(mediaArchive)
        val unzipped = JagArchive.decode(encoded)
        val newMediaArchive = MediaArchive(unzipped)

        newMediaArchive.files.values.forEach {
            val file = mediaArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
        assertEquals(mediaArchive.crc, newMediaArchive.crc)
        assertTrue(mediaArchive.zipped.contentEquals(newMediaArchive.zipped))
    }

    @Test
    fun `test encode and decode models archive`() {
        val modelsArchive = injector.getInstance<ModelsArchive>()

        val encoded = JagArchive.encode(modelsArchive)
        val unzipped = JagArchive.decode(encoded)
        val newModelsArchive = ModelsArchive(unzipped)

        newModelsArchive.files.values.forEach {
            val file = modelsArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
        assertEquals(modelsArchive.crc, newModelsArchive.crc)
        assertTrue(modelsArchive.zipped.contentEquals(newModelsArchive.zipped))
    }

    @Test
    fun `test encode and decode sounds archive`() {
        val soundsArchive = injector.getInstance<SoundsArchive>()

        val encoded = JagArchive.encode(soundsArchive)
        val unzipped = JagArchive.decode(encoded)
        val newSoundsArchive = SoundsArchive(unzipped)

        newSoundsArchive.files.values.forEach {
            val file = soundsArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
        assertEquals(soundsArchive.crc, newSoundsArchive.crc)
        assertTrue(soundsArchive.zipped.contentEquals(newSoundsArchive.zipped))
    }

    @Test
    fun `test encode and decode textures archive`() {
        val texturesArchive = injector.getInstance<TexturesArchive>()

        val encoded = JagArchive.encode(texturesArchive)
        val unzipped = JagArchive.decode(encoded)
        val newTexturesArchive = TexturesArchive(unzipped)

        newTexturesArchive.files.values.forEach {
            val file = texturesArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
        assertEquals(texturesArchive.crc, newTexturesArchive.crc)
        assertTrue(texturesArchive.zipped.contentEquals(newTexturesArchive.zipped))
    }

    @Test
    fun `test encode and decode title archive`() {
        val titleArchive = injector.getInstance<TitleArchive>()

        val encoded = JagArchive.encode(titleArchive)
        val unzipped = JagArchive.decode(encoded)
        val newTitleArchive = TitleArchive(unzipped)

        newTitleArchive.files.values.forEach {
            val file = titleArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
        assertEquals(titleArchive.crc, newTitleArchive.crc)
        assertTrue(titleArchive.zipped.contentEquals(newTitleArchive.zipped))
    }

    @Test
    fun `test encode and decode wordenc archive`() {
        val wordEncArchive = injector.getInstance<WordEncArchive>()

        val encoded = JagArchive.encode(wordEncArchive)
        val unzipped = JagArchive.decode(encoded)
        val newWordEncArchive = WordEncArchive(unzipped)

        newWordEncArchive.files.values.forEach {
            val file = wordEncArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
        assertEquals(wordEncArchive.crc, newWordEncArchive.crc)
        assertTrue(wordEncArchive.zipped.contentEquals(newWordEncArchive.zipped))
    }
}
