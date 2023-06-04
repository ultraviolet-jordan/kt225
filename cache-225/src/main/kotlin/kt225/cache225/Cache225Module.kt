package kt225.cache225

import dev.misfitlabs.kotlinguice4.KotlinModule
import kt225.cache.archive.config.varp.Varps
import kt225.cache225.config.varp.VarpsProvider

/**
 * @author Jordan Abraham
 */
class Cache225Module : KotlinModule() {
    override fun configure() {
        bind<Varps>().toProvider<VarpsProvider>()
    }
}
