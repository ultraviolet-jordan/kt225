package kt225.cache.archive

import com.google.inject.Provider
import kt225.common.buffer.RSByteBuffer

/**
 * @author Jordan Abraham
 */
interface EntryProvider<E : EntryType, T : MutableMap<Int, E>> : Provider<T> {
    fun decode(buffer: RSByteBuffer, type: E): E
    fun encode(type: E): RSByteBuffer
}
