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
import kt225.common.buffer.flip
import kt225.common.game.world.Coordinates
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocLayer
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocShape
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * @author Jordan Abraham
 */
class TestMapSquares {
    
    @Test
    fun `test sums`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquareLands = injector.getInstance<MapSquareLands<MapSquareLandEntryType>>()
        val mapSquareLocs = injector.getInstance<MapSquareLocs<MapSquareLocEntryType>>()

        assertEquals(4997120, mapSquareLands.values.sumOf { it.lands.size })
        assertEquals(537124, mapSquareLocs.values.sumOf { it.locs.size })
    }
    
    @Test
    fun `test map land encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquareLands = injector.getInstance<MapSquareLands<MapSquareLandEntryType>>()
        val mapSquareLandsProvider = injector.getInstance<MapSquareLandsProvider>()
        
        val lumbridge = mapSquareLands[12850]!!
        val buffer = ByteBuffer.allocate(50_000)
        mapSquareLandsProvider.encode(buffer, lumbridge)
        
        val decoded = mapSquareLandsProvider.decode(buffer.flip, MapSquareLandEntryType(lumbridge.mapSquare))
        assertEquals(lumbridge.mapSquare, decoded.mapSquare)
        assertEquals(lumbridge.lands.size, decoded.lands.size)
        assertContentEquals(lumbridge.lands, decoded.lands)
    }

    @Test
    fun `test all map land encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquareLands = injector.getInstance<MapSquareLands<MapSquareLandEntryType>>()
        val mapSquareLandsProvider = injector.getInstance<MapSquareLandsProvider>()

        for (mapSquareEntry in mapSquareLands) {
            val entry = mapSquareEntry.value
            val buffer = ByteBuffer.allocate(100_000)
            mapSquareLandsProvider.encode(buffer, entry)

            val decoded = mapSquareLandsProvider.decode(buffer.flip, MapSquareLandEntryType(entry.mapSquare))
            assertEquals(entry.mapSquare, decoded.mapSquare)
            assertEquals(entry.lands.size, decoded.lands.size)
            assertContentEquals(entry.lands, decoded.lands)
        }
    }

    @Test
    fun `test map loc encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquareLocs = injector.getInstance<MapSquareLocs<MapSquareLocEntryType>>()
        val mapSquareLocsProvider = injector.getInstance<MapSquareLocsProvider>()

        val lumbridge = mapSquareLocs[12850]!!
        
        val buffer = ByteBuffer.allocate(50_000)
        mapSquareLocsProvider.encode(buffer, lumbridge)

        val decoded = mapSquareLocsProvider.decode(buffer.flip, MapSquareLocEntryType(lumbridge.mapSquare))
        assertEquals(lumbridge.mapSquare, decoded.mapSquare)
        assertEquals(lumbridge.locs.size, decoded.locs.size)
        assertContentEquals(lumbridge.locs.toLongArray(), decoded.locs.toLongArray())
        repeat(lumbridge.locs.size) {
            assertEquals(lumbridge.locs[it], decoded.locs[it])
        }
    }

    @Test
    fun `test all map loc encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquareLocs = injector.getInstance<MapSquareLocs<MapSquareLocEntryType>>()
        val mapSquareLocsProvider = injector.getInstance<MapSquareLocsProvider>()
        
        for (mapSquareEntry in mapSquareLocs) {
            val entry = mapSquareEntry.value
            val buffer = ByteBuffer.allocate(50_000)
            mapSquareLocsProvider.encode(buffer, entry)

            val decoded = mapSquareLocsProvider.decode(buffer.flip, MapSquareLocEntryType(entry.mapSquare))
            assertEquals(entry.mapSquare, decoded.mapSquare)
            assertEquals(entry.locs.size, decoded.locs.size)
            assertContentEquals(entry.locs.toLongArray(), decoded.locs.toLongArray())
            repeat(entry.locs.size) {
                assertEquals(entry.locs[it], decoded.locs[it])
            }
        }
    }

    @Test
    fun `test maps rewrite lands`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapLands = injector.getInstance<MapLands>()
        val mapSquareLands = injector.getInstance<MapSquareLands<MapSquareLandEntryType>>()
        val mapSquareLandsProvider = injector.getInstance<MapSquareLandsProvider>()
        
        mapSquareLandsProvider.write(mapSquareLands)
        
        val newMapLands = MapLandsProvider().get()
        val newMapSquareLandsProvider = MapSquareLandsProvider(newMapLands)
        val newMapSquareLands = newMapSquareLandsProvider.get()
        
        newMapLands.forEachIndexed { index, resource ->
            val original = mapLands[index]
            assertEquals(original.id, resource.id)
            assertEquals(original.name, resource.name)
            assertEquals(original.x, resource.x)
            assertEquals(original.z, resource.z)
            assertEquals(original.crc, resource.crc)
        }
        
        newMapSquareLands.forEach { 
            val original = mapSquareLands[it.key]
            assertEquals(original?.mapSquare, it.value.mapSquare)
            assertContentEquals(original?.lands, it.value.lands)
        }
    }

    @Test
    fun `test maps rewrite locs`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapLocs = injector.getInstance<MapLocs>()
        val mapSquareLocs = injector.getInstance<MapSquareLocs<MapSquareLocEntryType>>()
        val mapSquareLocsProvider = injector.getInstance<MapSquareLocsProvider>()

        mapSquareLocsProvider.write(mapSquareLocs)

        val newMapLocs = MapLocsProvider().get()
        val newMapSquareLocsProvider = MapSquareLocsProvider(newMapLocs)
        val newMapSquareLocs = newMapSquareLocsProvider.get()

        newMapLocs.forEachIndexed { index, resource ->
            val original = mapLocs[index]
            assertEquals(original.id, resource.id)
            assertEquals(original.name, resource.name)
            assertEquals(original.x, resource.x)
            assertEquals(original.z, resource.z)
            assertEquals(original.crc, resource.crc)
        }

        newMapSquareLocs.forEach {
            val original = mapSquareLocs[it.key]
            assertEquals(original?.mapSquare, it.value.mapSquare)
            assertEquals(original?.locs?.size, it.value.locs.size)
            assertContentEquals(original?.locs?.toLongArray(), it.value.locs.toLongArray())
        }
    }

    @Test
    fun `test edit remove bush add chest`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquareLocs = injector.getInstance<MapSquareLocs<MapSquareLocEntryType>>()
        val mapSquareLocsProvider = injector.getInstance<MapSquareLocsProvider>()

        val lumbridge = mapSquareLocs[12850]!!
        
        lumbridge.query(Coordinates(3223, 3220, 0), MapSquareLocLayer.GROUND) {
            requireNotNull(it)
            assertEquals(1124, it.id)
            val removed = lumbridge.removeLoc(it)
            assertTrue(removed)
        }
        
        lumbridge.queryWithCoordinates(Coordinates(3223, 3222, 0), MapSquareLocLayer.GROUND) { loc, coord ->
            assertNull(loc)
            val chest = MapSquareLoc(
                id = 2191,
                shape = MapSquareLocShape.CENTREPIECE_STRAIGHT,
                rotation = MapSquareLocRotation.WEST,
                coords = coord,
            )
            val added = lumbridge.addLoc(chest)
            assertTrue(added)
        }

        mapSquareLocsProvider.write(mapSquareLocs)

        // Uncomment this if you want to write the file out to use in the game.
        /*val mapLocs = injector.getInstance<MapLocs>()
        val mapResource = mapLocs.first { it.id == 12850 }
        val bytes = mapResource.bytes
        val file = File("./${mapResource.name}")
        file.writeBytes(bytes)*/
    }
}
