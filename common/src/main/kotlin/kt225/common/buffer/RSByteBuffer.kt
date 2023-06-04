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

    fun readUByte(): Int = buffer.get().toInt() and 0xFF

    fun readUShort(): Int = buffer.short.toInt() and 0xFFFF

    fun readU24BitInt(): Int = (buffer.get().toInt() and 0xFF shl 16) or (buffer.short.toInt() and 0xFFFF)

    fun readInt(): Int = buffer.int

    fun remaining(): Int = buffer.remaining()

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
}
