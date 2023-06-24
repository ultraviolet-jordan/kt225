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
    private val title: Title,
    private val config: Config,
    private val inter: Interface,
    private val media: Media,
    private val models: Models,
    private val textures: Textures,
    private val wordenc: WordEnc,
    private val sounds: Sounds
) : Provider<Cache> {
    override fun get(): Cache {
        val jagFiles = mapOf(
            "none" to null,
            "title" to title,
            "config" to config,
            "interface" to inter,
            "media" to media,
            "models" to models,
            "textures" to textures,
            "wordenc" to wordenc,
            "sounds" to sounds
        )
        return Cache(jagFiles)
    }
}
