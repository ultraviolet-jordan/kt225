package kt225.cache225.map

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.map.MapLands
import kt225.cache.map.MapLandsProvider
import kt225.cache.map.MapLocs
import kt225.cache.map.MapLocsProvider
import kt225.cache.map.MapSquareLands
import kt225.cache.map.MapSquareLocs
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
    fun `test map land encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquares = injector.getInstance<MapSquareLands<MapSquareLandEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquareLandsProvider>()
        
        val lumbridge = mapSquares[12850]!!
        val buffer = ByteBuffer.allocate(50_000)
        mapSquaresProvider.encode(buffer, lumbridge)
        buffer.flip()
        
        val decoded = mapSquaresProvider.decode(buffer, MapSquareLandEntryType(lumbridge.mapSquare))
        assertEquals(lumbridge.mapSquare, decoded.mapSquare)
        assertEquals(lumbridge.lands.size, decoded.lands.size)
        assertContentEquals(lumbridge.lands, decoded.lands)
    }

    @Test
    fun `test all map land encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquares = injector.getInstance<MapSquareLands<MapSquareLandEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquareLandsProvider>()

        for (mapSquareEntry in mapSquares) {
            val entry = mapSquareEntry.value
            val buffer = ByteBuffer.allocate(100_000)
            mapSquaresProvider.encode(buffer, entry)
            buffer.flip()

            val decoded = mapSquaresProvider.decode(buffer, MapSquareLandEntryType(entry.mapSquare))
            assertEquals(entry.mapSquare, decoded.mapSquare)
            assertEquals(entry.lands.size, decoded.lands.size)
            assertContentEquals(entry.lands, decoded.lands)
        }
    }

    @Test
    fun `test map loc encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquares = injector.getInstance<MapSquareLocs<MapSquareLocEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquareLocsProvider>()

        val lumbridge = mapSquares[12850]!!
        
        val buffer = ByteBuffer.allocate(50_000)
        mapSquaresProvider.encode(buffer, lumbridge)
        buffer.flip()

        val decoded = mapSquaresProvider.decode(buffer, MapSquareLocEntryType(lumbridge.mapSquare))
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
        val mapSquares = injector.getInstance<MapSquareLocs<MapSquareLocEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquareLocsProvider>()
        
        for (mapSquareEntry in mapSquares) {
            val entry = mapSquareEntry.value
            val buffer = ByteBuffer.allocate(50_000)
            mapSquaresProvider.encode(buffer, entry)
            buffer.flip()

            val decoded = mapSquaresProvider.decode(buffer, MapSquareLocEntryType(entry.mapSquare))
            assertEquals(entry.mapSquare, decoded.mapSquare)
            assertEquals(entry.locs.size, decoded.locs.size)
            assertContentEquals(entry.locs.keys.toIntArray(), decoded.locs.keys.toIntArray())
            for (loc in entry.locs) {
                assertContentEquals(loc.value, decoded.locs[loc.key])
            }
        }
    }

    @Test
    fun `test maps rewrite lands`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val maps = injector.getInstance<MapLands>()
        val mapSquares = injector.getInstance<MapSquareLands<MapSquareLandEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquareLandsProvider>()
        
        mapSquaresProvider.write(mapSquares)
        
        val newMapsProvider = MapLandsProvider().get()
        val newMapSquaresProvider = MapSquareLandsProvider(newMapsProvider)
        val newMapSquares = newMapSquaresProvider.read()
        
        newMapsProvider.forEachIndexed { index, resource ->
            val original = maps[index]
            assertEquals(original.id, resource.id)
            assertEquals(original.name, resource.name)
            assertEquals(original.x, resource.x)
            assertEquals(original.z, resource.z)
            assertEquals(original.crc, resource.crc)
        }
        
        newMapSquares.forEach { 
            val original = mapSquares[it.key]
            assertEquals(original?.mapSquare, it.value.mapSquare)
            assertContentEquals(original?.lands, it.value.lands)
        }
    }

    @Test
    fun `test maps rewrite locs`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val maps = injector.getInstance<MapLocs>()
        val mapSquares = injector.getInstance<MapSquareLocs<MapSquareLocEntryType>>()
        val mapSquaresProvider = injector.getInstance<MapSquareLocsProvider>()

        mapSquaresProvider.write(mapSquares)

        val newMapsProvider = MapLocsProvider().get()
        val newMapSquaresProvider = MapSquareLocsProvider(newMapsProvider)
        val newMapSquares = newMapSquaresProvider.read()

        newMapsProvider.forEachIndexed { index, resource ->
            val original = maps[index]
            assertEquals(original.id, resource.id)
            assertEquals(original.name, resource.name)
            assertEquals(original.x, resource.x)
            assertEquals(original.z, resource.z)
            assertEquals(original.crc, resource.crc)
        }

        newMapSquares.forEach {
            val original = mapSquares[it.key]
            assertEquals(original?.mapSquare, it.value.mapSquare)
            assertEquals(original?.locs?.size, it.value.locs.size)
            assertContentEquals(original?.locs?.keys?.toIntArray(), it.value.locs.keys.toIntArray())
        }
    }
}
