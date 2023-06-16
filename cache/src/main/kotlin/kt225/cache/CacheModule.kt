package kt225.cache

import dev.misfitlabs.kotlinguice4.KotlinModule
import kt225.cache.archive.config.ConfigArchive
import kt225.cache.archive.config.ConfigArchiveProvider
import kt225.cache.archive.inter.InterfaceArchive
import kt225.cache.archive.inter.InterfaceArchiveProvider
import kt225.cache.archive.media.MediaArchive
import kt225.cache.archive.media.MediaArchiveProvider
import kt225.cache.archive.models.ModelsArchive
import kt225.cache.archive.models.ModelsArchiveProvider
import kt225.cache.archive.sounds.SoundsArchive
import kt225.cache.archive.sounds.SoundsArchiveProvider
import kt225.cache.archive.textures.TexturesArchive
import kt225.cache.archive.textures.TexturesArchiveProvider
import kt225.cache.archive.title.TitleArchive
import kt225.cache.archive.title.TitleArchiveProvider
import kt225.cache.archive.wordenc.WordEncArchive
import kt225.cache.archive.wordenc.WordEncArchiveProvider
import kt225.cache.map.MapProvider
import kt225.cache.map.Maps
import kt225.cache.song.Songs
import kt225.cache.song.SongsProvider

/**
 * @author Jordan Abraham
 */
object CacheModule : KotlinModule() {
    override fun configure() {
        bind<Cache>().toProvider<CacheProvider>().asEagerSingleton()
        bind<ConfigArchive>().toProvider<ConfigArchiveProvider>().asEagerSingleton()
        bind<InterfaceArchive>().toProvider<InterfaceArchiveProvider>().asEagerSingleton()
        bind<MediaArchive>().toProvider<MediaArchiveProvider>().asEagerSingleton()
        bind<ModelsArchive>().toProvider<ModelsArchiveProvider>().asEagerSingleton()
        bind<SoundsArchive>().toProvider<SoundsArchiveProvider>().asEagerSingleton()
        bind<TexturesArchive>().toProvider<TexturesArchiveProvider>().asEagerSingleton()
        bind<TitleArchive>().toProvider<TitleArchiveProvider>().asEagerSingleton()
        bind<WordEncArchive>().toProvider<WordEncArchiveProvider>().asEagerSingleton()
        bind<Songs>().toProvider<SongsProvider>().asEagerSingleton()
        bind<Maps>().toProvider<MapProvider>().asEagerSingleton()
    }
}
