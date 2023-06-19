package kt225.common.buffer

import java.math.BigInteger
import java.nio.ByteBuffer
import kotlin.math.min

/**
 * @author Jordan Abraham
 */
fun ByteBuffer.g1(): Int {
    return get().toInt() and 0xff
}

fun ByteBuffer.g1b(): Int {
    return get().toInt()
}

fun ByteBuffer.g2(): Int {
    return getShort().toInt() and 0xffff
}

fun ByteBuffer.g3(): Int {
    return (get().toInt() and 0xff shl 16) or (getShort().toInt() and 0xffff)
}

fun ByteBuffer.g4(): Int {
    return getInt()
}

fun ByteBuffer.g8(): Long {
    return getLong()
}

fun ByteBuffer.gsmarts(): Int {
    return if ((this[position()].toInt() and 0xff) < 128) g1() else g2() - 32768
}

fun ByteBuffer.gsmart(): Int {
    return if ((this[position()].toInt() and 0xff) < 128) g1() - 64 else g2() - 49152
}

fun ByteBuffer.gstr(): String {
    return String(gdata(toByte(10))).also {
        skip(1)
    }
}

fun ByteBuffer.gdata(size: Int = limit(), position: Int = position(), length: Int = size): ByteArray {
    val array = ByteArray(size).also {
        get(position, it, 0, length) // Doesn't move position.
    }
    skip(length)
    return array
}

fun ByteBuffer.p1(value: Int) {
    put(value.toByte())
}

fun ByteBuffer.p2(value: Int) {
    putShort(value.toShort())
}

fun ByteBuffer.p3(value: Int) {
    put((value shr 16).toByte())
    putShort(value.toShort())
}

fun ByteBuffer.p4(value: Int) {
    putInt(value)
}

fun ByteBuffer.p8(value: Long) {
    putLong(value)
}

fun ByteBuffer.pjstr(value: String) {
    put(value.toByteArray())
    put(10)
}

fun ByteBuffer.pdata(bytes: ByteArray, position: Int = position(), length: Int = bytes.size) {
    put(position, bytes, 0, length) // Doesn't move position.
    skip(length)
}

fun ByteBuffer.rsadec(exponent: BigInteger, modulus: BigInteger): ByteArray {
    val length = g1()
    val rsa = gdata(length)
    return BigInteger(rsa).modPow(exponent, modulus).toByteArray()
}

inline fun ByteBuffer.withBitAccess(function: ByteBuffer.() -> Unit) {
    position(position() * 8)
    // The mark. The all powerful. Keeps track of the current byte index.
    // Avoids total object creation annihilation since player/npc update uses this every tick.
    // Contemporary solutions use some type of helper "BitAccess" class to wrap the buffer for bit accessing.
    // Some of these solutions also instantiate a ByteArray(4026) for writing bits??
    mark()
    function.invoke(this)
    val position = position()
    reset()
    // The marked starting byte index to calculate from the ending position.
    val marked = position()
    val index = marked + (position - marked)
    position((index + 7) / 8)
}

fun ByteBuffer.pBit(count: Int, value: Int) {
    val position = position()
    // Constantly mark and reset.
    reset()
    val marked = position()
    // Keeps the mark positioned at the starting byte index.
    mark()
    val index = marked + (position - marked)
    pBit(value, count, index shr 3, index % 8)
    position(position + count)
}

private tailrec fun ByteBuffer.pBit(value: Int, remainingBits: Int, byteIndex: Int, bitIndex: Int) {
    if (remainingBits == 0) return
    val bitOffset = 8 - bitIndex
    // The maximum number of bits that can be written to the current byte.
    val bitsToWrite = min(remainingBits, bitOffset)
    val max = (1 shl bitsToWrite) - 1
    // The relevant bits from the value.
    val byteValue = (value ushr (remainingBits - bitsToWrite)) and max
    // The relevant bits in the current byte.
    // The runescape client pre generates this array.
    val mask = max shl (bitOffset - bitsToWrite)
    // The current byte from the buffer.
    val currentValue = get(byteIndex).toInt()
    // The current byte with the new bits.
    val newValue = currentValue and mask.inv() or (byteValue shl (bitOffset - bitsToWrite))
    put(byteIndex, newValue.toByte())
    return pBit(value, remainingBits - bitsToWrite, byteIndex + 1, 0)
}

fun ByteBuffer.skip(amount: Int) {
    position(position() + amount)
}

private tailrec fun ByteBuffer.toByte(terminator: Int, length: Int = 0): Int {
    if (this[position() + length].toInt() == terminator) return length
    return toByte(terminator, length + 1)
}
