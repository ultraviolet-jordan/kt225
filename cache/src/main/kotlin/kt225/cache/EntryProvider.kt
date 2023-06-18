package kt225.cache

import com.google.inject.Provider
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
interface EntryProvider<E : EntryType, T : MutableMap<Int, E>> : Provider<T> {
    fun read(): T
    fun write(entries: T)
    fun decode(buffer: ByteBuffer, entry: E): E
    fun encode(buffer: ByteBuffer, entry: E)
    override fun get(): T {
        return read()
    }
}
