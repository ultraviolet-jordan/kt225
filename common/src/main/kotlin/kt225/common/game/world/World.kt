package kt225.common.game.world

import kt225.common.game.entity.Entity
import kt225.common.game.entity.player.Player
import kt225.common.network.LoginRequest

/**
 * @author Jordan Abraham
 */
abstract class World(
    val players: Array<Player?>,
    val loginRequests: MutableSet<Entity>,
    val logoutRequests: MutableSet<Entity>
) {
    abstract fun start()
    abstract suspend fun requestLogin(request: LoginRequest)
    abstract fun processLoginRequests()
    abstract fun online(): Boolean
    abstract fun players(): Array<Player?>
    abstract fun stop()
}
