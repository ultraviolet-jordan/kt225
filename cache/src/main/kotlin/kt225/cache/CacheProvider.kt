package kt225.cache

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.config.Config
import kt225.cache.inter.Interface
import kt225.cache.media.Media
import kt225.cache.models.Models
import kt225.cache.sounds.Sounds
import kt225.cache.textures.Textures
import kt225.cache.title.Title
import kt225.cache.wordenc.WordEnc

/**
 * @author Jordan Abraham
 */
@Singleton
class CacheProvider @Inject constructor(
    private val titleArchive: Title,
    private val configArchive: Config,
    private val interfaceArchive: Interface,
    private val mediaArchive: Media,
    private val modelsArchive: Models,
    private val texturesArchive: Textures,
    private val wordEncArchive: WordEnc,
    private val soundsArchive: Sounds
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
