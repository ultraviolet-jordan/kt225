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
            "none" to null,
            "title" to titleArchive,
            "config" to configArchive,
            "interface" to interfaceArchive,
            "media" to mediaArchive,
            "models" to modelsArchive,
            "textures" to texturesArchive,
            "wordenc" to wordEncArchive,
            "sounds" to soundsArchive
        )
        return Cache(archives)
    }
}
