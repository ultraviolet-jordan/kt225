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
class JagArchiveUnzipped(
    val bytes: ByteArray,
    val name: String,
    val crc: Int,
    val files: Map<Int, JagArchiveFile> = TreeMap()
) {
    companion object {
        fun decode(bytes: ByteArray, name: String): JagArchiveUnzipped {
            val crc = CRC32()
            crc.update(bytes)
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
            val fileLength = buffer.g2()
            val files = buffer.decodeFiles(fileLength, needsDecompressing).filterNotNull().associateBy(JagArchiveFile::id)
            return JagArchiveUnzipped(bytes, name, crc.value.toInt(), files)
        }

        private tailrec fun ByteBuffer.decodeFiles(
            length: Int,
            needsDecompressing: Boolean,
            fileId: Int = 0,
            offset: Int = 8 + length * 10,
            files: Array<JagArchiveFile?> = arrayOfNulls(length)
        ): Array<JagArchiveFile?> {
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
            files[fileId] = JagArchiveFile(fileId, nameHash, data)
            return decodeFiles(length, needsDecompressing, fileId + 1, offset + compressedLength)
        }
    }
}
