package kt225.cache.map

import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class MapLocsProvider : MapsProvider<MapLocs> {
    override fun prefix(): String {
        return "l"
    }

    override fun resources(): MapLocs {
        return MapLocs()
    }
}
