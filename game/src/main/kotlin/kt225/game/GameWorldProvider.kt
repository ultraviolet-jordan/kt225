package kt225.game

import com.google.inject.Provider
import com.google.inject.Singleton
import kt225.common.game.world.World
import kt225.game.world.GameWorld

/**
 * @author Jordan Abraham
 */
@Singleton
class GameWorldProvider : Provider<World> {
    override fun get(): World = GameWorld()
}
