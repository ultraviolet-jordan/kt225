package kt225.game

import io.ktor.application.Application
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kt225.game.client.Client
import kt225.game.client.io.readLogin
import java.net.InetSocketAddress
import java.util.concurrent.Executors

/**
 * @author Jordan Abraham
 */
fun Application.installGameServer() = runBlocking {
    val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
    val selector = ActorSelectorManager(dispatcher)
    val server = aSocket(selector).tcp().bind(InetSocketAddress(43594))

    while (true) {
        val socket = server.accept()

        val client = Client(socket, socket.openReadChannel(), socket.openWriteChannel())
        launch(Dispatchers.IO) {
            client.readLogin()
        }
    }
}
