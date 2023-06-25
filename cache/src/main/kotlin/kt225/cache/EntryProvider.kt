package kt225.cache

import com.google.inject.Provider
import kt225.common.buffer.p1
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

    fun <T : Any?> ByteBuffer.pNotNull(value: T?, opcode: Int, function: ByteBuffer.(T) -> Unit) {
        if (value == null) {
            return
        }
        p1(opcode)
        function.invoke(this, value)
    }

    fun ByteBuffer.pTrue(value: Boolean, opcode: Int) {
        if (!value) {
            return
        }
        p1(opcode)
    }

    fun ByteBuffer.pFalse(value: Boolean, opcode: Int) {
        if (value) {
            return
        }
        p1(opcode)
    }

    fun ByteBuffer.pNotZero(value: Int, opcode: Int, function: ByteBuffer.(Int) -> Unit) {
        if (value == 0) {
            return
        }
        p1(opcode)
        function.invoke(this, value)
    }

    fun ByteBuffer.pNotNegative1(value: Int, opcode: Int, function: ByteBuffer.(Int) -> Unit) {
        if (value == -1) {
            return
        }
        p1(opcode)
        function.invoke(this, value)
    }
}
