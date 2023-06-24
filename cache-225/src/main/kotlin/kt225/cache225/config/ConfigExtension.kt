package kt225.cache225

import kt225.common.buffer.p1
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
fun <T : Any?> ByteBuffer.notNull(value: T?, opcode: Int, function: ByteBuffer.(T) -> Unit) {
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

fun ByteBuffer.pNonZero(value: Int, opcode: Int, function: ByteBuffer.(Int) -> Unit) {
    if (value == 0) {
        return
    }
    p1(opcode)
    function.invoke(this, value)
}
