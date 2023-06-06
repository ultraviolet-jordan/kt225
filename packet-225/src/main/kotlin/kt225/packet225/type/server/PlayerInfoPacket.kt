package kt225.packet225.type.server

import kt225.common.game.entity.player.Player
import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class PlayerInfoPacket(
    val observing: Player,
    val highDefinitionRenders: Array<ByteArray?>,
    val lowDefinitionRenders: Array<ByteArray?>
) : Packet {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerInfoPacket

        if (observing != other.observing) return false
        if (!highDefinitionRenders.contentDeepEquals(other.highDefinitionRenders)) return false
        return lowDefinitionRenders.contentDeepEquals(other.lowDefinitionRenders)
    }

    override fun hashCode(): Int {
        var result = observing.hashCode()
        result = 31 * result + highDefinitionRenders.contentDeepHashCode()
        result = 31 * result + lowDefinitionRenders.contentDeepHashCode()
        return result
    }
}
