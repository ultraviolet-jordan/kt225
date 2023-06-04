package kt225.common.buffer

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.apache.commons.compress.utils.IOUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class RSByteBuffer(
    private val buffer: ByteBuffer
) {
    constructor(array: ByteArray) : this(ByteBuffer.wrap(array))

    fun g1(): Int = buffer.get().toInt() and 0xFF
    fun g2(): Int = buffer.short.toInt() and 0xFFFF
    fun g3(): Int = (buffer.get().toInt() and 0xFF shl 16) or (buffer.short.toInt() and 0xFFFF)
    fun g4(): Int = buffer.int

    fun gstr(): String = String(readUChars(untilStringTerminator())).also {
        discard(1)
    }

    fun p1(value: Int) {
        buffer.put(value.toByte())
    }

    fun p2(value: Int) {
        buffer.putShort(value.toShort())
    }

    fun p3(value: Int) {
        buffer.put((value shr 16).toByte())
        buffer.putShort(value.toShort())
    }

    fun p4(value: Int) {
        buffer.putInt(value)
    }

    fun pjstr(value: String) {
        for (char in value) {
            buffer.put(char.code.toByte())
        }
        buffer.put(10)
    }

    fun remaining(): Int = buffer.remaining()

    fun discard(amount: Int) {
        buffer.position(buffer.position() + amount)
    }

    fun array(): ByteArray = buffer.array()

    fun flip(): RSByteBuffer {
        buffer.flip()
        return this
    }

    fun copyInto(
        array: ByteArray,
        destinationOffset: Int,
        startIndex: Int,
        endIndex: Int
    ): ByteArray = buffer.array().copyInto(array, destinationOffset, startIndex, endIndex)

    fun decompressBzip2(decompressedLength: Int, compressedLength: Int, startIndex: Int): ByteArray {
        val header = byteArrayOf(
            'B'.code.toByte(),
            'Z'.code.toByte(),
            'h'.code.toByte(),
            '1'.code.toByte()
        )
        val dest = ByteArray(decompressedLength + header.size + startIndex).also {
            header.copyInto(it, 0, 0, header.size)
            copyInto(it, header.size, startIndex, compressedLength + startIndex)
        }
        return ByteArrayOutputStream()
            .apply { BZip2CompressorInputStream(ByteArrayInputStream(dest)).use { IOUtils.copy(it, this) } }
            .toByteArray()
    }

    private tailrec fun untilStringTerminator(length: Int = 0): Int {
        if (buffer[buffer.position() + length].toInt() == 10) return length
        return untilStringTerminator(length + 1)
    }

    private fun readUChars(n: Int): CharArray = CharArray(n) { (buffer.get().toInt() and 0xFF).toChar() }
}
