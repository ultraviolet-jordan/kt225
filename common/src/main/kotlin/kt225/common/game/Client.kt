package kt225.common.game

import kt225.common.packet.Packet
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
interface Client {
    suspend fun acceptHandshake()
    suspend fun acceptLogin(buffer: ByteBuffer)
    suspend fun acceptGame(properties: Int, clientSeed: IntArray, serverSeed: IntArray, uid: Int, username: String, password: String)
    suspend fun writeLoginResponse(responseId: Int, flush: Boolean)
    suspend fun awaitPacket(): Packet?
    fun flushReadQueue()
    fun writePacket(packet: Packet)
    fun flushWriteQueue()
    fun flushWriteChannel()
    fun close()
}
