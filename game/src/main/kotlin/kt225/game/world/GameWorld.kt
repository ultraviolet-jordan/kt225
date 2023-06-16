package kt225.game.world

import kt225.common.game.entity.player.Player
import kt225.common.game.world.Position
import kt225.common.game.world.World
import kt225.common.network.LoginRequest
import kt225.game.GameClient
import kt225.game.entity.player.EntityPlayer
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
class GameWorld : World(
    players = arrayOfNulls(2048),
    loginRequests = ConcurrentHashMap.newKeySet(),
    logoutRequests = ConcurrentHashMap.newKeySet()
) {
    private var online = false

    override fun start() {
        this.online = true
    }

    override suspend fun requestLogin(request: LoginRequest) {
        val session = request.session
        val client = GameClient(
            serverSeed = request.serverSeed,
            clientSeed = request.clientSeed,
            session = session
        )
        val player = EntityPlayer(
            username = request.username,
            client = client,
            world = this
        )
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
            it.init(Position.Default)
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
