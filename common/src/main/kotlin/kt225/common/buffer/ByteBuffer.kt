package kt225.common.buffer

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.apache.commons.compress.utils.IOUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.nio.ByteBuffer
import kotlin.math.min

/**
 * @author Jordan Abraham
 */
fun ByteBuffer.g1(): Int = get().toInt() and 0xFF
fun ByteBuffer.g2(): Int = short.toInt() and 0xFFFF
fun ByteBuffer.g3(): Int = (get().toInt() and 0xFF shl 16) or (short.toInt() and 0xFFFF)
fun ByteBuffer.g4(): Int = int

fun ByteBuffer.gstr(): String = String(readUChars(untilStringTerminator())).also {
    discard(1)
}

fun ByteBuffer.gArrayBuffer(size: Int): ByteArray = ByteArray(size) { get() }

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
    for (char in value) {
        put(char.code.toByte())
    }
    put(10)
}

fun ByteBuffer.pArrayBuffer(bytes: ByteArray) {
    for (byte in bytes) {
        put(byte)
    }
}

fun ByteBuffer.discard(amount: Int) {
    position(position() + amount)
}

fun ByteBuffer.decompressBzip2(decompressedLength: Int, compressedLength: Int, startIndex: Int): ByteArray {
    val header = byteArrayOf(
        'B'.code.toByte(),
        'Z'.code.toByte(),
        'h'.code.toByte(),
        '1'.code.toByte()
    )
    val dest = ByteArray(decompressedLength + header.size + startIndex).also {
        header.copyInto(it, 0, 0, header.size)
        array().copyInto(it, header.size, startIndex, compressedLength + startIndex)
    }
    return ByteArrayOutputStream()
        .apply { BZip2CompressorInputStream(ByteArrayInputStream(dest)).use { IOUtils.copy(it, this) } }
        .toByteArray()
}

fun ByteBuffer.rsaDecrypt(exponent: BigInteger, modulus: BigInteger): ByteArray {
    val length = g1()
    val rsa = gArrayBuffer(length)
    return BigInteger(rsa).modPow(exponent, modulus).toByteArray()
}

private tailrec fun ByteBuffer.untilStringTerminator(length: Int = 0): Int {
    if (this[position() + length].toInt() == 10) return length
    return untilStringTerminator(length + 1)
}

private fun ByteBuffer.readUChars(n: Int): CharArray = CharArray(n) { (get().toInt() and 0xFF).toChar() }

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
