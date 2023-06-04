package kt225.common.game

import kt225.common.buffer.RSByteBuffer
import kt225.common.packet.Packet

/**
 * @author Jordan Abraham
 */
interface Client {
    suspend fun acceptHandshake()
    suspend fun acceptLogin(buffer: RSByteBuffer)
    suspend fun acceptGame(properties: Int, clientSeed: IntArray, serverSeed: IntArray, uid: Int, username: String, password: String)
    suspend fun writeLoginResponse(responseId: Int, flush: Boolean)
    suspend fun awaitPacket(): Packet?
    fun processReadPool()
    fun writePacket(packet: Packet)
    fun processWritePool()
    fun flushWriteChannel()
    fun close()
}
