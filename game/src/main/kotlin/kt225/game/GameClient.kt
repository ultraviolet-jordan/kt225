package kt225.game

import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel

/**
 * @author Jordan Abraham
 */
class GameClient(
    private val socket: Socket
) {
    private val readChannel = socket.openReadChannel()
    private val writeChannel = socket.openWriteChannel()
    val seed: Long = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()

    fun accept() {
    }

    fun disconnect() {
        socket.close()
    }
}
