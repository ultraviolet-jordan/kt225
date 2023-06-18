package kt225.cache

import kt225.common.buffer.bzip2Compress
import kt225.common.buffer.bzip2Decompress
import kt225.common.buffer.copy
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
        val bytes = buffer.copy(0, buffer.position()).array()
        val crc = CRC32()
        crc.update(bytes)
        return archive.files.put(fileId, JagArchiveFile(fileId, nameHash, crc.value.toInt(), bytes)) != null
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
            val crc = CRC32()
            crc.update(bytes)
            val input = ByteBuffer.wrap(bytes)
            require(input.remaining() >= 6)
            val decompressed = input.g3()
            val compressed = input.g3()
            val isCompressed = decompressed != compressed
            val buffer = when {
                isCompressed -> ByteBuffer.wrap(input.copy(size = compressed).bzip2Decompress())
                else -> input
            }
            if (isCompressed) {
                require(decompressed == buffer.capacity())
            }
            require(buffer.remaining() >= 2)
            val length = buffer.g2()
            val files = buffer.decodeFiles(length, isCompressed).filterNotNull().associateBy(JagArchiveFile::id).toMutableMap()
            require(length == files.size)
            return JagArchiveUnzipped(bytes, isCompressed, crc.value.toInt(), files)
        }

        fun encode(archive: JagArchiveUnzipped): ByteArray {
            val isCompressed = archive.isCompressed
            val files = ByteBuffer.allocate(1_500_000)
            files.p2(archive.files.size)
            val offset = files.encodeFiles(archive.files, isCompressed)
            val filesBytes = files.copy(0, offset).array()

            val buffer = ByteBuffer.allocate(1_500_000)
            buffer.p3(filesBytes.size)
            val bytes = when {
                isCompressed -> bzip2Compress(filesBytes)
                else -> filesBytes
            }
            buffer.p3(bytes.size)
            buffer.pdata(bytes)
            return buffer.copy(0, buffer.position()).array()
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
            require(remaining() >= 10)
            val nameHash = g4()
            val decompressed = g3()
            val compressed = g3()
            val data = when {
                isCompressed -> gdata(decompressed)
                else -> copy(offset, compressed).bzip2Decompress()
            }
            val crc = CRC32()
            crc.update(data)
            require(decompressed == data.size)
            val file = JagArchiveFile(fileId, nameHash, crc.value.toInt(), data)
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
            requireNotNull(file)
            p4(file.nameHash)
            p3(file.bytes.size)
            val bytes = when {
                isCompressed -> file.bytes
                else -> bzip2Compress(file.bytes)
            }
            p3(bytes.size)
            val position = position()
            pdata(bytes, offset) // This moves the position.
            position(position)
            return encodeFiles(files, isCompressed, fileId + 1, offset + bytes.size)
        }
    }
}
