package kt225.cache

import kt225.common.buffer.RSByteBuffer
import java.util.TreeMap
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
abstract class CacheArchive {
    private val files: MutableMap<Int, CacheFile> = TreeMap()
    private val crc32: CRC32 = CRC32()

    abstract fun name(): String

    fun decode(bytes: ByteArray) {
        crc32.update(bytes)
        val buffer = RSByteBuffer(bytes)
        require(buffer.remaining() >= 6)
        val decompressedLength = buffer.readU24BitInt()
        val compressedLength = buffer.readU24BitInt()
        val noCompression = decompressedLength == compressedLength

        val decompressed = when {
            noCompression -> buffer
            else -> RSByteBuffer(buffer.decompressBzip2(decompressedLength, compressedLength, 6))
        }

        require(decompressed.remaining() >= 2)
        val size = decompressed.readUShort()

        require(decompressed.remaining() >= size * 10)
        var offset = 8 + size * 10
        repeat(size) { fileId ->
            val fileNameHash = decompressed.readInt()
            val fileDecompressedLength = decompressed.readU24BitInt()
            val fileCompressedLength = decompressed.readU24BitInt()
            val fileData = when {
                noCompression -> decompressed.decompressBzip2(fileDecompressedLength, fileCompressedLength, offset)
                else -> ByteArray(fileDecompressedLength).also { decompressed.copyInto(it, 0, offset, fileDecompressedLength) }
            }
            files[fileId] = CacheFile(fileNameHash, fileDecompressedLength, fileCompressedLength, offset, fileData)
            offset += fileCompressedLength
        }
    }

    fun file(fileId: Int): CacheFile? = files[fileId]

    fun file(fileName: String): CacheFile? = files.values.firstOrNull { it.nameHash == fileName.nameHash() }

    fun crc(): Int = crc32.value.toInt()

    private fun String.nameHash() = uppercase().fold(0) { hash, char -> hash * 61 + char.code - 32 }
}
