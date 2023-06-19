package kt225.cache

import kt225.cache.bzip2.bzip2Compress
import kt225.cache.bzip2.bzip2Decompress
import kt225.common.buffer.g2
import kt225.common.buffer.g3
import kt225.common.buffer.g4
import kt225.common.buffer.gdata
import kt225.common.buffer.p2
import kt225.common.buffer.p3
import kt225.common.buffer.p4
import kt225.common.buffer.pdata
import java.nio.ByteBuffer
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
abstract class JagArchive(
    private val archive: JagArchiveUnzipped
) {
    val crc: Int get() = archive.crc
    val zipped: ByteArray get() = archive.bytes
    val lastFileId: Int get() = archive.files.keys.last()
    val lastFile: JagArchiveFile get() = archive.files.values.last()
    val files: MutableMap<Int, JagArchiveFile> get() = archive.files
    val isCompressed: Boolean get() = archive.isCompressed

    fun file(fileId: Int): JagArchiveFile? {
        return files[fileId]
    }

    fun file(fileName: String): JagArchiveFile? {
        return files.values.firstOrNull { it.nameHash == fileName.nameHash() }
    }
    fun read(fileId: Int): ByteBuffer? {
        return file(fileId)?.let { ByteBuffer.wrap(it.bytes) }
    }

    fun read(fileName: String): ByteBuffer? {
        return file(fileName)?.let { ByteBuffer.wrap(it.bytes) }
    }

    fun add(fileId: Int, buffer: ByteBuffer, nameHash: Int): Boolean {
        val bytes = buffer.gdata()
        val crc = CRC32().also { it.update(bytes) }.value.toInt()
        return files.put(fileId, JagArchiveFile(fileId, nameHash, crc, bytes)) == null
    }

    fun add(fileName: String, buffer: ByteBuffer): Boolean {
        val existing = file(fileName)
        if (existing != null) {
            require(existing.nameHash == fileName.nameHash())
            return add(existing.id, buffer, existing.nameHash)
        }
        return add(lastFileId + 1, buffer, fileName.nameHash())
    }

    fun remove(fileId: Int): Boolean {
        val existing = file(fileId) ?: return false
        val removed = files.remove(existing.id)
        return removed != null
    }

    fun remove(fileName: String): Boolean {
        val existing = file(fileName) ?: return false
        val removed = files.remove(existing.id)
        return removed != null
    }

    private fun String.nameHash(): Int {
        return uppercase().fold(0) { hash, char -> hash * 61 + char.code - 32 }
    }

    companion object {
        fun decode(bytes: ByteArray): JagArchiveUnzipped {
            val input = ByteBuffer.wrap(bytes)
            require(input.remaining() >= 6) { "Invalid input byte array." }

            val decompressed = input.g3()
            val compressed = input.g3()
            val isCompressed = decompressed != compressed

            val buffer = when {
                isCompressed -> ByteBuffer.wrap(bzip2Decompress(input.gdata(compressed)))
                else -> input
            }

            if (isCompressed) {
                require(decompressed == buffer.capacity()) { "Invalid compressed data." }
            }

            require(buffer.remaining() >= 2) { "Invalid buffer length." }

            val length = buffer.g2()
            val files = buffer
                .decodeFiles(length, isCompressed)
                .filterNotNull()
                .associateBy(JagArchiveFile::id)
                .toMutableMap()
            require(length == files.size) { "Invalid file count." }

            val crc = CRC32().also { it.update(bytes) }.value.toInt()
            return JagArchiveUnzipped(bytes, isCompressed, crc, files)
        }

        fun encode(archive: JagArchive): ByteArray {
            val isCompressed = archive.isCompressed
            val files = archive.files

            // Good enough as long as there is enough alloc.
            val lengths = files.values.sumOf { it.bytes.size + 10 + 2 }

            val filesBuffer = ByteBuffer.allocate(lengths)
            filesBuffer.p2(files.size)
            val offset = filesBuffer.encodeFiles(files, isCompressed)
            filesBuffer.position(offset)
            filesBuffer.flip()
            val filesBytes = filesBuffer.gdata(offset)

            val archiveBuffer = ByteBuffer.allocate(filesBytes.size + 6)
            archiveBuffer.p3(filesBytes.size)
            val bytes = when {
                isCompressed -> bzip2Compress(filesBytes)
                else -> filesBytes
            }
            archiveBuffer.p3(bytes.size)
            archiveBuffer.pdata(bytes)
            archiveBuffer.flip()
            return archiveBuffer.gdata()
        }

        private tailrec fun ByteBuffer.decodeFiles(
            length: Int,
            isCompressed: Boolean,
            fileId: Int = 0,
            offset: Int = 8 + length * 10,
            files: Array<JagArchiveFile?> = arrayOfNulls(length)
        ): Array<JagArchiveFile?> {
            if (fileId == length) {
                return files
            }
            require(remaining() >= 10) { "Invalid buffer length." }
            val nameHash = g4()
            val decompressed = g3()
            val compressed = g3()
            mark()
            val bytes = when {
                isCompressed -> gdata(decompressed)
                else -> bzip2Decompress(gdata(compressed, offset))
            }
            reset()
            require(decompressed == bytes.size) { "Invalid compressed data" }

            val crc = CRC32().also { it.update(bytes) }.value.toInt()
            val file = JagArchiveFile(fileId, nameHash, crc, bytes)
            files[fileId] = file
            return decodeFiles(length, isCompressed, fileId + 1, offset + compressed, files)
        }

        private tailrec fun ByteBuffer.encodeFiles(
            files: MutableMap<Int, JagArchiveFile>,
            isCompressed: Boolean,
            fileId: Int = 0,
            offset: Int = 2 + files.size * 10,
            accumulator: Int = 0
        ): Int {
            if (accumulator == files.size) {
                return offset
            }
            val file = files[fileId] ?: return encodeFiles(files, isCompressed, fileId + 1, offset, accumulator)
            p4(file.nameHash)
            p3(file.bytes.size)
            val bytes = when {
                isCompressed -> file.bytes
                else -> bzip2Compress(file.bytes)
            }
            p3(bytes.size)
            mark()
            pdata(bytes, offset) // This moves the position.
            reset()
            return encodeFiles(files, isCompressed, fileId + 1, offset + bytes.size, accumulator + 1)
        }
    }
}
