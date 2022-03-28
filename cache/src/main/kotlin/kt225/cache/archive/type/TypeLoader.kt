package kt225.cache.archive.type

import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
abstract class TypeLoader<T : Type> {
    val entries by lazy(::load)

    abstract fun load(): Map<Int, T>
    abstract fun ByteReadPacket.decode(type: T): T
    open fun postLoadEntryType(type: T) {}
}
