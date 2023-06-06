package kt225.cache.archive

import com.google.inject.Provider
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
interface EntryProvider<E : EntryType, T : MutableMap<Int, E>> : Provider<T> {
    fun decode(buffer: ByteBuffer, type: E): E
    fun encode(type: E): ByteBuffer
}
