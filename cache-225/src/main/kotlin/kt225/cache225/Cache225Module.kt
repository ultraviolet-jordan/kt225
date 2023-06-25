package kt225.cache225

import dev.misfitlabs.kotlinguice4.KotlinModule
import kt225.cache.config.flo.Flos
import kt225.cache.config.obj.Objs
import kt225.cache.config.varp.Varps
import kt225.cache.map.MapSquareLands
import kt225.cache.map.MapSquareLocs
import kt225.cache225.config.flo.FloEntryType
import kt225.cache225.config.flo.FlosProvider
import kt225.cache225.config.obj.ObjEntryType
import kt225.cache225.config.obj.ObjsProvider
import kt225.cache225.config.varp.VarpEntryType
import kt225.cache225.config.varp.VarpsProvider
import kt225.cache225.map.MapSquareLandEntryType
import kt225.cache225.map.MapSquareLandsProvider
import kt225.cache225.map.MapSquareLocEntryType
import kt225.cache225.map.MapSquareLocsProvider

/**
 * @author Jordan Abraham
 */
object Cache225Module : KotlinModule() {
    override fun configure() {
        // Eager ones required for game server.
        bind<Varps<VarpEntryType>>().toProvider<VarpsProvider>().asEagerSingleton()
        bind<MapSquareLands<MapSquareLandEntryType>>().toProvider<MapSquareLandsProvider>().asEagerSingleton()
        bind<MapSquareLocs<MapSquareLocEntryType>>().toProvider<MapSquareLocsProvider>().asEagerSingleton()
        bind<Objs<ObjEntryType>>().toProvider<ObjsProvider>().asEagerSingleton()
        
        // Lazy load ones for editing/reading etc.
        bind<Flos<FloEntryType>>().toProvider<FlosProvider>()
    }
}
