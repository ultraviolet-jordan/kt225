package kt225.cache.archive

import com.google.inject.Provider
import kt225.common.buffer.RSByteBuffer

/**
 * @author Jordan Abraham
 */
interface EntryProvider<E, T> : Provider<T> {
    fun RSByteBuffer.decode(type: E): E
}
