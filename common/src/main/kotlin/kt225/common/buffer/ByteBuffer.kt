package kt225.common.buffer

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import java.io.ByteArrayInputStream
import java.math.BigInteger
import java.nio.ByteBuffer
import kotlin.math.min

/**
 * @author Jordan Abraham
 */
fun ByteBuffer.g1(): Int {
    return get().toInt() and 0xff
}

fun ByteBuffer.g1s(): Int {
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

fun ByteBuffer.gSmart1or2(): Int {
    return if ((this[position()].toInt() and 0xff) < 128) g1() else g2() - 32768
}

fun ByteBuffer.gSmart1or2s(): Int {
    return if ((this[position()].toInt() and 0xff) < 128) g1() - 64 else g2() - 49152
}

fun ByteBuffer.gString(): String {
    return String(gArrayBuffer(toByte(10))).also {
        skip(1)
    }
}

fun ByteBuffer.gArrayBuffer(size: Int, position: Int = position()): ByteArray {
    val array = ByteArray(size).also {
        get(position, it, 0, size) // Doesn't move position.
    }
    skip(size)
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

fun ByteBuffer.pString(value: String) {
    put(value.toByteArray())
    put(10)
}

fun ByteBuffer.pArrayBuffer(bytes: ByteArray) {
    put(bytes)
}

fun ByteBuffer.skip(amount: Int) {
    position(position() + amount)
}

fun ByteBuffer.decompressBzip2(length: Int, startIndex: Int = position()): ByteArray {
    require(startIndex - 4 > 0)
    val startPosition = position()
    position(0)
    val dest = gArrayBuffer(length + 4, startIndex - 4).also {
        // Copy 4 + length bytes offset by -4 starting position.
        // The bzip header is copied to the beginning 4 bytes thanks to the -4 start.
        // Files are in bzip2 format however they do not contain the required header needed to decompress.
        // The client implementation does not require this header.
        it[0] = 'B'.code.toByte()
        it[1] = 'Z'.code.toByte()
        it[2] = 'h'.code.toByte()
        it[3] = '1'.code.toByte()
    }
    position(startPosition)
    val compressor = BZip2CompressorInputStream(ByteArrayInputStream(dest))
    val bytes = compressor.readAllBytes()
    compressor.close()
    return bytes
}

fun ByteBuffer.rsaDecrypt(exponent: BigInteger, modulus: BigInteger): ByteBuffer {
    val length = g1()
    val rsa = gArrayBuffer(length)
    return ByteBuffer.wrap(BigInteger(rsa).modPow(exponent, modulus).toByteArray())
}

private tailrec fun ByteBuffer.toByte(terminator: Int, length: Int = 0): Int {
    if (this[position() + length].toInt() == terminator) return length
    return toByte(terminator, length + 1)
}

inline fun ByteBuffer.withBitAccess(block: BitAccess.() -> Unit) {
    val bitAccess = BitAccess(this)
    block.invoke(bitAccess)
    position((bitAccess.bitIndex + 7) / 8)
}

class BitAccess(val buffer: ByteBuffer) {
    var bitIndex = buffer.position() * 8

    fun pBit(count: Int, value: Int) {
        pBit(value, count, bitIndex shr 3, bitIndex % 8)
        bitIndex += count
    }

    private tailrec fun pBit(value: Int, remainingBits: Int, byteIndex: Int, bitIndex: Int) {
        if (remainingBits == 0) return
        val bitOffset = 8 - bitIndex
        // The maximum number of bits that can be written to the current byte.
        val bitsToWrite = min(remainingBits, bitOffset)
        val max = (1 shl bitsToWrite) - 1
        // The relevant bits from the value.
        val byteValue = (value ushr (remainingBits - bitsToWrite)) and max
        // The relevant bits in the current byte.
        val mask = max shl (bitOffset - bitsToWrite)
        // The current byte from the buffer.
        val currentValue = buffer.get(byteIndex).toInt()
        // The current byte with the new bits.
        val newValue = currentValue and mask.inv() or (byteValue shl (bitOffset - bitsToWrite))
        buffer.put(byteIndex, newValue.toByte())
        return pBit(value, remainingBits - bitsToWrite, byteIndex + 1, 0)
    }
}
