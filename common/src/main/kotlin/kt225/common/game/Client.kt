package kt225.common.game

import kt225.common.buffer.RSByteBuffer

/**
 * @author Jordan Abraham
 */
interface Client {
    suspend fun accept()
    suspend fun readLogin(buffer: RSByteBuffer)
    suspend fun writeLogin()
    fun close()
}
