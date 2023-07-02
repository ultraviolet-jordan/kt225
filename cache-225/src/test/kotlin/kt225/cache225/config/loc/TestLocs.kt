package kt225.cache225.config.loc

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.config.loc.Locs
import kt225.cache225.Cache225Module
import kotlin.test.Test

/**
 * @author Jordan Abraham
 */
class TestLocs {
    @Test
    fun `test locs`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val locs = injector.getInstance<Locs<LocEntryType>>()
        locs.values.forEach(::println)
    }
}
