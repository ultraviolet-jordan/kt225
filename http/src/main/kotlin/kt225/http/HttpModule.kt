package kt225.http

import dev.misfitlabs.kotlinguice4.KotlinModule
import dev.misfitlabs.kotlinguice4.multibindings.KotlinMultibinder
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.routing.Routing
import kt225.http.routing.ApplicationRouting
import kt225.http.routing.client.ClientRouting
import kt225.http.routing.config.ConfigResource
import kt225.http.routing.config.ConfigResourceProvider
import kt225.http.routing.config.ConfigRouting
import kt225.http.routing.crc.CRCRouting
import kt225.http.routing.inter.InterfaceResource
import kt225.http.routing.inter.InterfaceResourceProvider
import kt225.http.routing.inter.InterfaceRouting
import kt225.http.routing.media.MediaResource
import kt225.http.routing.media.MediaResourceProvider
import kt225.http.routing.media.MediaRouting
import kt225.http.routing.models.ModelsResource
import kt225.http.routing.models.ModelsResourceProvider
import kt225.http.routing.models.ModelsRouting
import kt225.http.routing.songs.SongsRouting
import kt225.http.routing.sounds.SoundsResource
import kt225.http.routing.sounds.SoundsResourceProvider
import kt225.http.routing.sounds.SoundsRouting
import kt225.http.routing.textures.TexturesResource
import kt225.http.routing.textures.TexturesResourceProvider
import kt225.http.routing.textures.TexturesRouting
import kt225.http.routing.title.TitleResource
import kt225.http.routing.title.TitleResourceProvider
import kt225.http.routing.title.TitleRouting
import kt225.http.routing.wordenc.WordEncResource
import kt225.http.routing.wordenc.WordEncResourceProvider
import kt225.http.routing.wordenc.WordEncRouting

/**
 * @author Jordan Abraham
 */
class HttpModule(
    private val args: Array<String>
) : KotlinModule() {
    override fun configure() {
        bind<ApplicationArguments>().toInstance(ApplicationArguments(args))
        bind<ApplicationEnvironment>().toProvider<ApplicationEnvironmentProvider>().asEagerSingleton()
        bind<Routing>().toProvider<RoutingProvider>()
        bind<ApplicationEngine>().toProvider<ApplicationEngineProvider>().asEagerSingleton()

        bind<ConfigResource>().toProvider<ConfigResourceProvider>()
        bind<InterfaceResource>().toProvider<InterfaceResourceProvider>()
        bind<MediaResource>().toProvider<MediaResourceProvider>()
        bind<ModelsResource>().toProvider<ModelsResourceProvider>()
        bind<SoundsResource>().toProvider<SoundsResourceProvider>()
        bind<TexturesResource>().toProvider<TexturesResourceProvider>()
        bind<TitleResource>().toProvider<TitleResourceProvider>()
        bind<WordEncResource>().toProvider<WordEncResourceProvider>()
        
        KotlinMultibinder.newSetBinder<ApplicationRouting>(kotlinBinder).apply {
            addBinding().to<ClientRouting>()
            addBinding().to<ConfigRouting>()
            addBinding().to<CRCRouting>()
            addBinding().to<InterfaceRouting>()
            addBinding().to<MediaRouting>()
            addBinding().to<ModelsRouting>()
            addBinding().to<SongsRouting>()
            addBinding().to<SoundsRouting>()
            addBinding().to<TexturesRouting>()
            addBinding().to<TitleRouting>()
            addBinding().to<WordEncRouting>()
        }
    }
}
