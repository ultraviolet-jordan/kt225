package kt225.game.world.map

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.config.loc.Locs
import kt225.cache.map.MapSquareLands
import kt225.cache.map.MapSquareLocs
import kt225.cache225.config.loc.LocEntryType
import kt225.cache225.map.MapSquareLandEntryType
import kt225.cache225.map.MapSquareLocEntryType
import org.rsmod.pathfinder.StepValidator
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
@Singleton
class CollisionManagerProvider @Inject constructor(
    private val mapSquareLands: MapSquareLands<MapSquareLandEntryType>,
    private val mapSquareLocs: MapSquareLocs<MapSquareLocEntryType>,
    private val zoneFlags: ZoneFlags,
    private val stepValidator: StepValidator,
    private val locs: Locs<LocEntryType>
) : Provider<CollisionManager> {
    override fun get(): CollisionManager {
        return CollisionManager(mapSquareLands, mapSquareLocs, zoneFlags, stepValidator, locs)
    }
}
