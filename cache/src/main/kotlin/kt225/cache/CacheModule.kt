package kt225.cache

import dev.misfitlabs.kotlinguice4.KotlinModule
import kt225.cache.config.Config
import kt225.cache.config.ConfigProvider
import kt225.cache.inter.Interface
import kt225.cache.inter.InterfaceProvider
import kt225.cache.map.MapLands
import kt225.cache.map.MapLandsProvider
import kt225.cache.map.MapLocs
import kt225.cache.map.MapLocsProvider
import kt225.cache.media.Media
import kt225.cache.media.MediaProvider
import kt225.cache.models.Models
import kt225.cache.models.ModelsProvider
import kt225.cache.song.Songs
import kt225.cache.song.SongsProvider
import kt225.cache.sounds.Sounds
import kt225.cache.sounds.SoundsProvider
import kt225.cache.textures.Textures
import kt225.cache.textures.TexturesProvider
import kt225.cache.title.Title
import kt225.cache.title.TitleProvider
import kt225.cache.wordenc.WordEnc
import kt225.cache.wordenc.WordEncProvider

/**
 * @author Jordan Abraham
 */
object CacheModule : KotlinModule() {
    override fun configure() {
        bind<Cache>().toProvider<CacheProvider>().asEagerSingleton()
        bind<Config>().toProvider<ConfigProvider>().asEagerSingleton()
        bind<Interface>().toProvider<InterfaceProvider>().asEagerSingleton()
        bind<Media>().toProvider<MediaProvider>().asEagerSingleton()
        bind<Models>().toProvider<ModelsProvider>().asEagerSingleton()
        bind<Sounds>().toProvider<SoundsProvider>().asEagerSingleton()
        bind<Textures>().toProvider<TexturesProvider>().asEagerSingleton()
        bind<Title>().toProvider<TitleProvider>().asEagerSingleton()
        bind<WordEnc>().toProvider<WordEncProvider>().asEagerSingleton()
        bind<Songs>().toProvider<SongsProvider>().asEagerSingleton()
        bind<MapLands>().toProvider<MapLandsProvider>().asEagerSingleton()
        bind<MapLocs>().toProvider<MapLocsProvider>().asEagerSingleton()
    }
}
