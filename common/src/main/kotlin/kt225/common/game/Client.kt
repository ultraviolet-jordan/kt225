package kt225.common.game

import kt225.common.crypto.IsaacRandom
import kt225.common.game.entity.player.Player
import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
abstract class Client(
    val serverIsaac: IsaacRandom,
    val clientIsaac: IsaacRandom
) {
    var player: Player? = null

    abstract fun writePacket(packet: Packet)
    abstract fun readPacket(packet: Packet)
    abstract fun flushWriteQueue()
    abstract fun consumeReadQueue()
    abstract fun writePacketDirect(packet: Packet, length: Int)

    fun attach(player: Player) {
        this.player = player
    }
}
