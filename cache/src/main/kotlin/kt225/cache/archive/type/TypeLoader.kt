package kt225.cache.archive.type

import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
abstract class TypeLoader<T : Type> {
    protected val entries = mutableMapOf<Int, T>()

    abstract fun ByteReadPacket.decode(type: T): T
    open fun postLoadEntryType(type: T) {}

    fun size() = entries.size
    fun entries(): Collection<T> = entries.values
}
