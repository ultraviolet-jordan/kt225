package kt225.common.game

import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
interface Client {
    suspend fun acceptHandshake()
    suspend fun acceptLogin(buffer: RSByteBuffer)
    suspend fun writeLoginResponse(responseId: Int, flush: Boolean)
    suspend fun awaitPacket(): Packet?
    suspend fun readPacketId(): Int
    suspend fun readPacketSize(length: Int): Int
    fun processReadPool()
    fun writePacket(packet: Packet)
    fun processWritePool()
    fun flushWriteChannel()
    fun close()
}
