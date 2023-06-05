package kt225.common.game.world

import kt225.common.game.entity.player.Player
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
abstract class World {
    protected val players: ArrayList<Player> = ArrayList()
    protected val loginRequests: ConcurrentHashMap.KeySetView<Player, Boolean> = ConcurrentHashMap.newKeySet()
    protected val logoutRequest: ConcurrentHashMap.KeySetView<Player, Boolean> = ConcurrentHashMap.newKeySet()

    abstract fun start()
    abstract fun requestLogin(player: Player)
    abstract fun requestLogout(player: Player)
    abstract fun processLoginRequests()
    abstract fun processLogoutRequests()
    abstract fun online(): Boolean
    abstract fun players(): List<Player>
    abstract fun stop()
}
