package kt225.game

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.map.MapSquares
import kt225.cache225.map.MapSquareEntryType
import kt225.common.game.world.World
import kt225.game.world.GameWorld

/**
 * @author Jordan Abraham
 */
@Singleton
class GameWorldProvider @Inject constructor(
    private val mapSquares: MapSquares<MapSquareEntryType>
) : Provider<World> {
    override fun get(): World = GameWorld(mapSquares)
}
