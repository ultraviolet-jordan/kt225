package kt225.cache225.map

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.map.MapSquares
import kt225.cache225.Cache225Module
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class TestMapSquares {

    @Test
    fun `test map squares`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquares = injector.getInstance<MapSquares<MapSquareEntryType>>()

        val map = mapSquares[12850] ?: return
        println(mapSquares.size)
    }
    
    @Test
    fun `test map land encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquares = injector.getInstance<MapSquares<MapSquareEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquaresProvider>()
        
        val lumbridge = mapSquares[12850]!!
        val buffer = ByteBuffer.allocate(100_000)
        lumbridge.type = 0
        mapSquaresProvider.encode(buffer, lumbridge)
        buffer.flip()
        
        val landDecoded = mapSquaresProvider.decode(buffer, MapSquareEntryType(lumbridge.mapSquare, type = 0))
        assertEquals(lumbridge.mapSquare, landDecoded.mapSquare)
        assertEquals(lumbridge.lands.size, landDecoded.lands.size)
        assertContentEquals(lumbridge.lands, landDecoded.lands)
    }

    @Test
    fun `test map loc encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquares = injector.getInstance<MapSquares<MapSquareEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquaresProvider>()

        val lumbridge = mapSquares[12850]!!
        
        val buffer = ByteBuffer.allocate(500_000)
        lumbridge.type = 1
        mapSquaresProvider.encode(buffer, lumbridge)
        buffer.flip()

        val landDecoded = mapSquaresProvider.decode(buffer, MapSquareEntryType(lumbridge.mapSquare, type = 1))
        assertEquals(lumbridge.mapSquare, landDecoded.mapSquare)
        assertEquals(lumbridge.locs.size, landDecoded.locs.size)
        assertContentEquals(lumbridge.locs.keys.toIntArray(), landDecoded.locs.keys.toIntArray())
        assertContentEquals(lumbridge.locs.values, lumbridge.locs.values)
    }
}
