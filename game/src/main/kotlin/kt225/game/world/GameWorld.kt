package kt225.game.world

import kt225.common.game.entity.player.Player
import kt225.common.game.world.Position
import kt225.common.game.world.World

/**
 * @author Jordan Abraham
 */
class GameWorld : World() {
    private val players: Array<Player?> = arrayOfNulls(2048)
    private var online = false

    override fun start() {
        this.online = true
    }

    override fun requestLogin(player: Player) {
        loginRequests.add(player)
    }

    override fun requestLogout(player: Player) {
        logoutRequest.add(player)
    }

    override fun processLoginRequests() {
        if (!online) return
        if (loginRequests.isEmpty()) return

        loginRequests.forEach {
            players[players.indexOf(null)] = it
            it.init(Position.Default)
            it.login()
        }
        loginRequests.clear()
    }

    override fun processLogoutRequests() {
        if (!online) return
        if (logoutRequest.isEmpty()) return
        // TODO Logout requests
    }

    override fun online(): Boolean = online

    override fun players(): Array<Player?> = players

    override fun stop() {
        this.online = false
    }
}
