package kt225.cache225.map

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.map.MapSquares
import kt225.cache.map.Maps
import kt225.cache.map.MapsProvider
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
        val buffer = ByteBuffer.allocate(50_000)
        lumbridge.type = 0
        mapSquaresProvider.encode(buffer, lumbridge)
        buffer.flip()
        
        val decoded = mapSquaresProvider.decode(buffer, MapSquareEntryType(lumbridge.mapSquare, type = 0))
        assertEquals(lumbridge.mapSquare, decoded.mapSquare)
        assertEquals(lumbridge.lands.size, decoded.lands.size)
        assertContentEquals(lumbridge.lands, decoded.lands)
    }

    @Test
    fun `test all map land encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquares = injector.getInstance<MapSquares<MapSquareEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquaresProvider>()

        for (mapSquareEntry in mapSquares) {
            val entry = mapSquareEntry.value
            val buffer = ByteBuffer.allocate(100_000)
            entry.type = 0
            mapSquaresProvider.encode(buffer, entry)
            buffer.flip()

            val decoded = mapSquaresProvider.decode(buffer, MapSquareEntryType(entry.mapSquare, type = 0))
            assertEquals(entry.mapSquare, decoded.mapSquare)
            assertEquals(entry.lands.size, decoded.lands.size)
            assertContentEquals(entry.lands, decoded.lands)
        }
    }

    @Test
    fun `test map loc encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquares = injector.getInstance<MapSquares<MapSquareEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquaresProvider>()

        val lumbridge = mapSquares[12850]!!
        
        val buffer = ByteBuffer.allocate(50_000)
        lumbridge.type = 1
        mapSquaresProvider.encode(buffer, lumbridge)
        buffer.flip()

        val decoded = mapSquaresProvider.decode(buffer, MapSquareEntryType(lumbridge.mapSquare, type = 1))
        assertEquals(lumbridge.mapSquare, decoded.mapSquare)
        assertEquals(lumbridge.locs.size, decoded.locs.size)
        assertContentEquals(lumbridge.locs.keys.toIntArray(), decoded.locs.keys.toIntArray())
        for (loc in lumbridge.locs) {
            assertContentEquals(loc.value, decoded.locs[loc.key])
        }
    }

    @Test
    fun `test all map loc encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquares = injector.getInstance<MapSquares<MapSquareEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquaresProvider>()
        
        for (mapSquareEntry in mapSquares) {
            val entry = mapSquareEntry.value
            val buffer = ByteBuffer.allocate(50_000)
            entry.type = 1
            mapSquaresProvider.encode(buffer, entry)
            buffer.flip()

            val decoded = mapSquaresProvider.decode(buffer, MapSquareEntryType(entry.mapSquare, type = 1))
            assertEquals(entry.mapSquare, decoded.mapSquare)
            assertEquals(entry.locs.size, decoded.locs.size)
            assertContentEquals(entry.locs.keys.toIntArray(), decoded.locs.keys.toIntArray())
            for (loc in entry.locs) {
                assertContentEquals(loc.value, decoded.locs[loc.key])
            }
        }
    }

    @Test
    fun `test maps rewrite`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val maps = injector.getInstance<Maps>()
        val mapSquares = injector.getInstance<MapSquares<MapSquareEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquaresProvider>()
        
        mapSquaresProvider.write(mapSquares)
        
        val newMapsProvider = MapsProvider().get()
        val newMapSquaresProvider = MapSquaresProvider(newMapsProvider)
        val newMapSquares = newMapSquaresProvider.read()
        
        newMapsProvider.forEachIndexed { index, resource ->
            val original = maps[index]
            assertEquals(original.id, resource.id)
            assertEquals(original.name, resource.name)
            assertEquals(original.x, resource.x)
            assertEquals(original.z, resource.z)
            assertEquals(original.crc, resource.crc)
            assertEquals(original.type, resource.type)
        }
        
        newMapSquares.forEach { 
            val original = mapSquares[it.key]
            assertEquals(original?.mapSquare, it.value.mapSquare)
            assertEquals(original?.locs?.size, it.value.locs.size)
            assertContentEquals(original?.lands, it.value.lands)
            assertContentEquals(original?.locs?.keys?.toIntArray(), it.value.locs.keys.toIntArray())
        }
    }
}
