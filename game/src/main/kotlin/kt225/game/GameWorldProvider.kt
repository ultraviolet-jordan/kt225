package kt225.game

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.common.game.world.World
import kt225.game.world.GameWorld
import kt225.game.world.map.CollisionManager

/**
 * @author Jordan Abraham
 */
@Singleton
class GameWorldProvider @Inject constructor(
    private val collisionManager: CollisionManager
) : Provider<World> {
    override fun get(): World = GameWorld(collisionManager)
}
