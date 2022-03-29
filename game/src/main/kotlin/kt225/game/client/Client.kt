package kt225.game.client

import io.ktor.network.sockets.Socket
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.close

/**
 * @author Jordan Abraham
 */
class Client(
    private val socket: Socket,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel,
    val seed: Long = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
) {
    fun disconnect() {
        writeChannel.close()
        socket.close()
    }
}
