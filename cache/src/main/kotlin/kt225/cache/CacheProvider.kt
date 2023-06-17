package kt225.cache

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.archive.config.ConfigArchive
import kt225.cache.archive.inter.InterfaceArchive
import kt225.cache.archive.media.MediaArchive
import kt225.cache.archive.models.ModelsArchive
import kt225.cache.archive.sounds.SoundsArchive
import kt225.cache.archive.textures.TexturesArchive
import kt225.cache.archive.title.TitleArchive
import kt225.cache.archive.wordenc.WordEncArchive

/**
 * @author Jordan Abraham
 */
@Singleton
class CacheProvider @Inject constructor(
    private val titleArchive: TitleArchive,
    private val configArchive: ConfigArchive,
    private val interfaceArchive: InterfaceArchive,
    private val mediaArchive: MediaArchive,
    private val modelsArchive: ModelsArchive,
    private val texturesArchive: TexturesArchive,
    private val wordEncArchive: WordEncArchive,
    private val soundsArchive: SoundsArchive
) : Provider<Cache> {
    override fun get(): Cache {
        val archives = mapOf(
            0 to null,
            1 to titleArchive,
            2 to configArchive,
            3 to interfaceArchive,
            4 to mediaArchive,
            5 to modelsArchive,
            6 to texturesArchive,
            7 to wordEncArchive,
            8 to soundsArchive
        )
        return Cache(archives)
    }
}
