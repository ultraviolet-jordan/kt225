package kt225.game.world.map

import com.google.inject.Provider
import com.google.inject.Singleton
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
@Singleton
class ZoneFlagsProvider : Provider<ZoneFlags> {
    override fun get(): ZoneFlags {
        return ZoneFlags()
    }
}
