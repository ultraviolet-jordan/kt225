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
import kt225.common.game.world.Position
import kt225.common.game.world.map.MapSquareLoc
import kt225.common.game.world.map.MapSquareLocLayer
import kt225.common.game.world.map.MapSquareLocRotation
import kt225.common.game.world.map.MapSquareLocShape
import kt225.common.game.world.map.MapSquarePosition
import java.io.File
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Jordan Abraham
 */
class TestMapSquares {
    @Test
    fun `test map land encoder decoder`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquareLands = injector.getInstance<MapSquareLands<MapSquareLandEntryType>>()
        val mapSquareLandsProvider = injector.getInstance<MapSquareLandsProvider>()
        
        val lumbridge = mapSquareLands[12850]!!
        val buffer = ByteBuffer.allocate(50_000)
        mapSquareLandsProvider.encode(buffer, lumbridge)
        buffer.flip()
        
        val decoded = mapSquareLandsProvider.decode(buffer, MapSquareLandEntryType(lumbridge.mapSquare))
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
            buffer.flip()

            val decoded = mapSquareLandsProvider.decode(buffer, MapSquareLandEntryType(entry.mapSquare))
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
        buffer.flip()

        val decoded = mapSquareLocsProvider.decode(buffer, MapSquareLocEntryType(lumbridge.mapSquare))
        assertEquals(lumbridge.mapSquare, decoded.mapSquare)
        assertEquals(lumbridge.locs.size, decoded.locs.size)
        assertContentEquals(lumbridge.locs.keys.toIntArray(), decoded.locs.keys.toIntArray())
        for (loc in lumbridge.locs) {
            assertEquals(loc.value, decoded.locs[loc.key])
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
            buffer.flip()

            val decoded = mapSquareLocsProvider.decode(buffer, MapSquareLocEntryType(entry.mapSquare))
            assertEquals(entry.mapSquare, decoded.mapSquare)
            assertEquals(entry.locs.size, decoded.locs.size)
            assertContentEquals(entry.locs.keys.toIntArray(), decoded.locs.keys.toIntArray())
            for (loc in entry.locs) {
                assertEquals(loc.value, decoded.locs[loc.key])
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
            assertContentEquals(original?.locs?.keys?.toIntArray(), it.value.locs.keys.toIntArray())
        }
    }

    @Test
    fun `test edit remove bush add chest`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val mapSquareLocs = injector.getInstance<MapSquareLocs<MapSquareLocEntryType>>()
        val mapSquareLocsProvider = injector.getInstance<MapSquareLocsProvider>()

        val lumbridge = mapSquareLocs[12850]!!
        
        lumbridge.getLoc(Position(3223, 3220, 0), MapSquareLocLayer.GROUND) { bush, position ->
            requireNotNull(bush)
            assertEquals(1124, bush.id)
            val removed = lumbridge.removeLoc(bush, position)
            assertTrue(removed)
        }
        
        val chestPosition = Position(3223, 3222, 0)
        val chest = MapSquareLoc(
            id = 2191,
            shape = MapSquareLocShape.CENTREPIECE_STRAIGHT,
            rotation = MapSquareLocRotation.SOUTH
        )
        
        val added = lumbridge.addLoc(chest, MapSquarePosition(chestPosition.localX, chestPosition.localZ, chestPosition.plane))
        assertTrue(added)

        mapSquareLocsProvider.write(mapSquareLocs)

        // Uncomment this if you want to write the file out to use in the game.
        val mapLocs = injector.getInstance<MapLocs>()
        val mapResource = mapLocs.first { it.id == 12850 }
        val bytes = mapResource.bytes
        val file = File("./${mapResource.name}")
        file.writeBytes(bytes)
    }
}
