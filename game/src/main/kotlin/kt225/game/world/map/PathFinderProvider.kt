package kt225.game.world.map

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import org.rsmod.pathfinder.PathFinder
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
@Singleton
class PathFinderProvider @Inject constructor(
    private val zoneFlags: ZoneFlags
) : Provider<PathFinder> {
    override fun get(): PathFinder {
        return PathFinder(
            flags = zoneFlags.flags,
            useRouteBlockerFlags = false
        )
    }
}
