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
    fun crc(): Int {
        return archive.crc
    }

    fun zippedBytes(): ByteArray {
        return archive.bytes
    }

    fun unzipped(): JagArchiveUnzipped {
        return archive
    }

    fun file(fileId: Int): JagArchiveFile? {
        return archive.files[fileId]
    }

    fun file(fileName: String): JagArchiveFile? {
        return archive.files.values.firstOrNull { it.nameHash == fileName.nameHash() }
    }

    fun files(): MutableMap<Int, JagArchiveFile> {
        return archive.files
    }

    fun read(fileId: Int): ByteBuffer? {
        return file(fileId)?.let { ByteBuffer.wrap(it.bytes) }
    }

    fun read(fileName: String): ByteBuffer? {
        return file(fileName)?.let { ByteBuffer.wrap(it.bytes) }
    }

    fun write(fileId: Int, buffer: ByteBuffer, nameHash: Int): Boolean {
        val bytes = buffer.gdata()
        val crc = CRC32().also { it.update(bytes) }.value.toInt()
        return archive.files.put(fileId, JagArchiveFile(fileId, nameHash, crc, bytes)) != null
    }

    fun write(fileName: String, buffer: ByteBuffer): Boolean {
        val existing = file(fileName)
        if (existing != null) {
            return write(existing.id, buffer, existing.nameHash)
        }
        return write(lastFileId() + 1, buffer, -1)
    }

    fun lastFileId(): Int {
        return archive.files.keys.last()
    }

    fun lastFile(): JagArchiveFile {
        return archive.files.values.last()
    }

    fun isCompressed(): Boolean {
        return archive.isCompressed
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
            val files = buffer.decodeFiles(length, isCompressed).filterNotNull().associateBy(JagArchiveFile::id).toMutableMap()
            require(length == files.size) { "Invalid file count." }

            val crc = CRC32().also { it.update(bytes) }.value.toInt()
            return JagArchiveUnzipped(bytes, isCompressed, crc, files)
        }

        fun encode(archive: JagArchiveUnzipped): ByteArray {
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
            return decodeFiles(length, isCompressed, fileId + 1, offset + compressed, files.plus(file))
        }

        private tailrec fun ByteBuffer.encodeFiles(
            files: MutableMap<Int, JagArchiveFile>,
            isCompressed: Boolean,
            fileId: Int = 0,
            offset: Int = 2 + files.size * 10
        ): Int {
            if (fileId == files.size) {
                return offset
            }
            val file = files[fileId]
            requireNotNull(file) { "File not found." }
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
            return encodeFiles(files, isCompressed, fileId + 1, offset + bytes.size)
        }
    }
}
