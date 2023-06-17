package kt225.cache225.map

import com.google.inject.Guice
import com.google.inject.Injector
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.map.MapSquares
import kt225.cache225.Cache225Module
import kotlin.test.Test

/**
 * @author Jordan Abraham
 */
class TestMapSquares {

    @Test
    fun `test map squares`() {
        val injector: Injector = Guice.createInjector(CacheModule, Cache225Module)
        val maps = injector.getInstance<MapSquares<MapSquareEntryType>>()

        val map = maps[12850] ?: return
        val land = map.lands
        println(maps.size)
    }
}
