package kt225.cache225.config

import kt225.common.buffer.p1
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
inline fun <reified T : Any?> ByteBuffer.pNotNull(value: T?, opcode: Int, function: ByteBuffer.(T) -> Unit) {
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
