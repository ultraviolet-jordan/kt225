package kt225.cache

import kt225.common.buffer.decompressBzip2
import kt225.common.buffer.g2
import kt225.common.buffer.g3
import kt225.common.buffer.g4
import java.nio.ByteBuffer
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
        val input = ByteBuffer.wrap(bytes)
        require(input.remaining() >= 6)
        val decompressedLength = input.g3()
        val compressedLength = input.g3()
        val needsDecompressing = decompressedLength != compressedLength

        val buffer = when {
            needsDecompressing -> input.decompressBzip2(compressedLength, 6)
            else -> input
        }

        if (needsDecompressing) {
            require(decompressedLength == buffer.capacity())
        }

        require(buffer.remaining() >= 2)
        buffer.decodeFiles(buffer.g2(), needsDecompressing).also {
            files.putAll(it.filterNotNull().associateBy(CacheFile::id))
        }
    }

    private tailrec fun ByteBuffer.decodeFiles(
        length: Int,
        needsDecompressing: Boolean,
        fileId: Int = 0,
        offset: Int = 8 + length * 10,
        files: Array<CacheFile?> = arrayOfNulls(length)
    ): Array<CacheFile?> {
        if (fileId == length) {
            return files
        }
        require(remaining() >= 10)
        val nameHash = g4()
        val decompressedLength = g3()
        val compressedLength = g3()
        val data = when {
            needsDecompressing -> ByteArray(decompressedLength).also { array().copyInto(it, 0, offset, decompressedLength) }
            else -> decompressBzip2(compressedLength, offset).array()
        }
        require(decompressedLength == data.size)
        files[fileId] = CacheFile(fileId, nameHash, decompressedLength, compressedLength, offset, data)
        return decodeFiles(length, needsDecompressing, fileId + 1, offset + compressedLength)
    }

    fun file(fileId: Int): CacheFile? = files[fileId]

    fun file(fileName: String): CacheFile? = files.values.firstOrNull { it.nameHash == fileName.nameHash() }

    fun crc(): Int = crc32.value.toInt()

    private fun String.nameHash() = uppercase().fold(0) { hash, char -> hash * 61 + char.code - 32 }
}
