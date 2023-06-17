package kt225.cache225

import dev.misfitlabs.kotlinguice4.KotlinModule
import kt225.cache.archive.config.varp.Varps
import kt225.cache.map.MapSquares
import kt225.cache225.config.varp.VarpEntryType
import kt225.cache225.config.varp.VarpsProvider
import kt225.cache225.map.MapSquareEntryType
import kt225.cache225.map.MapSquaresProvider

/**
 * @author Jordan Abraham
 */
object Cache225Module : KotlinModule() {
    override fun configure() {
        bind<Varps<VarpEntryType>>().toProvider<VarpsProvider>().asEagerSingleton()
        bind<MapSquares<MapSquareEntryType>>().toProvider<MapSquaresProvider>().asEagerSingleton()
    }
}
