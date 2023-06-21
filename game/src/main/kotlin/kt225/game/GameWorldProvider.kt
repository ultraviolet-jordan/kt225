package kt225.game

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.cache.map.MapSquareLands
import kt225.cache.map.MapSquareLocs
import kt225.cache225.map.MapSquareLandEntryType
import kt225.cache225.map.MapSquareLocEntryType
import kt225.common.game.world.World
import kt225.game.world.GameWorld

/**
 * @author Jordan Abraham
 */
@Singleton
class GameWorldProvider @Inject constructor(
    private val mapSquareLands: MapSquareLands<MapSquareLandEntryType>,
    private val mapSquareLocs: MapSquareLocs<MapSquareLocEntryType>
) : Provider<World> {
    override fun get(): World = GameWorld(mapSquareLands, mapSquareLocs)
}
