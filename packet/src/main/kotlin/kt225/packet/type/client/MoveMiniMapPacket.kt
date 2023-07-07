package kt225.packet.type.client

import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
data class MoveMiniMapPacket(
    val ctrlDown: Int,
    val destinationX: Int,
    val destinationZ: Int
) : Packet
