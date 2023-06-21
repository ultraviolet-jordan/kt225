package kt225.cache.map

import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class MapLandsProvider : MapsProvider<MapLands> {
    override fun prefix(): String {
        return "m"
    }

    override fun resources(): MapLands {
        return MapLands()
    }
}
