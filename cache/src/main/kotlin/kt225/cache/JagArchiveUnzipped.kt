package kt225.cache

import kt225.common.buffer.bzip2Decompress
import kt225.common.buffer.g2
import kt225.common.buffer.g3
import kt225.common.buffer.g4
import kt225.common.buffer.gArrayBuffer
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
    val files: MutableMap<Int, JagArchiveFile> = TreeMap()
) {
    companion object {
        fun decode(bytes: ByteArray, name: String): JagArchiveUnzipped {
            val crc = CRC32()
            crc.update(bytes)

            val input = ByteBuffer.wrap(bytes)
            require(input.remaining() >= 6)

            val decompressed = input.g3()
            val compressed = input.g3()
            val isDecompress = decompressed != compressed

            val buffer = when {
                isDecompress -> ByteBuffer.wrap(input.bzip2Decompress(compressed))
                else -> input
            }
            if (isDecompress) {
                require(decompressed == buffer.capacity())
            }

            require(buffer.remaining() >= 2)
            val length = buffer.g2()
            val files = buffer.decodeFiles(length, isDecompress).filterNotNull().associateBy(JagArchiveFile::id).toMutableMap()
            require(length == files.size)
            return JagArchiveUnzipped(bytes, name, crc.value.toInt(), files)
        }

        private tailrec fun ByteBuffer.decodeFiles(
            length: Int,
            isDecompress: Boolean,
            fileId: Int = 0,
            offset: Int = 8 + length * 10,
            files: Array<JagArchiveFile?> = arrayOfNulls(length)
        ): Array<JagArchiveFile?> {
            if (fileId == length) {
                return files
            }

            require(remaining() >= 10)
            val nameHash = g4()
            val decompressed = g3()
            val compressed = g3()

            val position = position()
            val data = when {
                isDecompress -> gArrayBuffer(decompressed)
                else -> bzip2Decompress(compressed, offset)
            }
            position(position)
            require(decompressed == data.size)
            val file = JagArchiveFile(fileId, nameHash, data)
            return decodeFiles(length, isDecompress, fileId + 1, offset + compressed, files.plus(file))
        }
    }
}
