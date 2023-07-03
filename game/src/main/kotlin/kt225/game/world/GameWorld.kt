package kt225.game.world

import kt225.common.game.entity.player.Player
import kt225.common.game.world.Coordinates
import kt225.common.game.world.World
import kt225.common.network.LoginRequest
import kt225.game.GameClient
import kt225.game.entity.player.EntityPlayer
import kt225.game.world.map.CollisionManager
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
class GameWorld(
    val collisionManager: CollisionManager
) : World(
    players = arrayOfNulls(2048),
    loginRequests = ConcurrentHashMap.newKeySet(),
    logoutRequests = ConcurrentHashMap.newKeySet()
) {
    private var online = false

    override fun start() {
        this.online = true
    }

    override suspend fun requestLogin(request: LoginRequest) {
        val (_, _, username, _, clientSeed, serverSeed, session) = request
        val client = GameClient(serverSeed, clientSeed, session)
        val player = EntityPlayer(this, username, client)
        if (loginRequests.add(player)) {
            session.attach(client)
            client.attach(player)
        }
    }

    override fun processLoginRequests() {
        if (!online) return
        if (loginRequests.isEmpty()) return

        loginRequests.forEach {
            require(it is Player)
            players[players.indexOf(null)] = it
            it.init(Coordinates.DEFAULT)
            it.login()
        }
        loginRequests.clear()
    }

    override fun online(): Boolean {
        return online
    }

    override fun players(): Array<Player?> {
        return players
    }

    override fun stop() {
        this.online = false
    }
}
