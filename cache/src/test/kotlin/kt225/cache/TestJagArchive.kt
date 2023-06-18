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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Jordan Abraham
 */
class TestJagArchive {
    @Test
    fun `test encode and decode config archive`() {
        val injector = Guice.createInjector(CacheModule)
        val configArchive = injector.getInstance<ConfigArchive>()

        val encoded = JagArchive.encode(configArchive.unzipped())
        val unzipped = JagArchive.decode(encoded)
        val newConfigArchive = ConfigArchive(unzipped)

        newConfigArchive.files().values.forEach {
            val file = configArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }

        assertEquals(configArchive.crc(), newConfigArchive.crc())
    }

    @Test
    fun `test encode and decode interface archive`() {
        val injector = Guice.createInjector(CacheModule)
        val interfaceArchive = injector.getInstance<InterfaceArchive>()

        val encoded = JagArchive.encode(interfaceArchive.unzipped())
        val unzipped = JagArchive.decode(encoded)
        val newInterfaceArchive = InterfaceArchive(unzipped)

        newInterfaceArchive.files().values.forEach {
            val file = interfaceArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
    }

    @Test
    fun `test encode and decode media archive`() {
        val injector = Guice.createInjector(CacheModule)
        val mediaArchive = injector.getInstance<MediaArchive>()

        val encoded = JagArchive.encode(mediaArchive.unzipped())
        val unzipped = JagArchive.decode(encoded)
        val newMediaArchive = MediaArchive(unzipped)

        newMediaArchive.files().values.forEach {
            val file = mediaArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
    }

    @Test
    fun `test encode and decode models archive`() {
        val injector = Guice.createInjector(CacheModule)
        val modelsArchive = injector.getInstance<ModelsArchive>()

        val encoded = JagArchive.encode(modelsArchive.unzipped())
        val unzipped = JagArchive.decode(encoded)
        val newModelsArchive = ModelsArchive(unzipped)

        newModelsArchive.files().values.forEach {
            val file = modelsArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
    }

    @Test
    fun `test encode and decode sounds archive`() {
        val injector = Guice.createInjector(CacheModule)
        val soundsArchive = injector.getInstance<SoundsArchive>()

        val encoded = JagArchive.encode(soundsArchive.unzipped())
        val unzipped = JagArchive.decode(encoded)
        val newSoundsArchive = SoundsArchive(unzipped)

        newSoundsArchive.files().values.forEach {
            val file = soundsArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
    }

    @Test
    fun `test encode and decode textures archive`() {
        val injector = Guice.createInjector(CacheModule)
        val texturesArchive = injector.getInstance<TexturesArchive>()

        val encoded = JagArchive.encode(texturesArchive.unzipped())
        val unzipped = JagArchive.decode(encoded)
        val newTexturesArchive = TexturesArchive(unzipped)

        newTexturesArchive.files().values.forEach {
            val file = texturesArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
    }

    @Test
    fun `test encode and decode title archive`() {
        val injector = Guice.createInjector(CacheModule)
        val titleArchive = injector.getInstance<TitleArchive>()

        val encoded = JagArchive.encode(titleArchive.unzipped())
        val unzipped = JagArchive.decode(encoded)
        val newTitleArchive = TitleArchive(unzipped)

        newTitleArchive.files().values.forEach {
            val file = titleArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
    }

    @Test
    fun `test encode and decode wordenc archive`() {
        val injector = Guice.createInjector(CacheModule)
        val wordEncArchive = injector.getInstance<WordEncArchive>()

        val encoded = JagArchive.encode(wordEncArchive.unzipped())
        val unzipped = JagArchive.decode(encoded)
        val newWordEncArchive = WordEncArchive(unzipped)

        newWordEncArchive.files().values.forEach {
            val file = wordEncArchive.file(it.id)
            assertEquals(file?.id, it.id)
            file?.bytes?.contentEquals(it.bytes)?.let(::assertTrue)
            assertEquals(file?.nameHash, it.nameHash)
            assertEquals(file?.crc, it.crc)
        }
    }
}
