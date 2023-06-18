package kt225.common.game

import com.runetopic.cryptography.isaac.ISAAC
import kt225.common.game.entity.player.Player
import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
abstract class Client(
    val serverIsaac: ISAAC,
    val clientIsaac: ISAAC
) {
    var player: Player? = null

    abstract fun writePacket(packet: Packet)
    abstract fun readPacket(packet: Packet)
    abstract fun flushWriteQueue()
    abstract fun consumeReadQueue()

    fun attach(player: Player) {
        this.player = player
    }
}
