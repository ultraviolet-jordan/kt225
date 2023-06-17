package kt225.cache

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
abstract class JagArchive(
    private val archive: JagArchiveUnzipped
) {
    fun crc(): Int {
        return archive.crc
    }

    fun name(): String {
        return archive.name
    }

    fun zipped(): ByteArray {
        return archive.bytes
    }

    fun file(fileId: Int): JagArchiveFile? {
        return archive.files[fileId]
    }

    fun file(fileName: String): JagArchiveFile? {
        return archive.files.values.firstOrNull { it.nameHash == fileName.nameHash() }
    }

    fun read(fileId: Int): ByteBuffer? {
        return file(fileId)?.let { ByteBuffer.wrap(it.data) }
    }

    fun read(fileName: String): ByteBuffer? {
        return file(fileName)?.let { ByteBuffer.wrap(it.data) }
    }

    private fun String.nameHash(): Int {
        return uppercase().fold(0) { hash, char -> hash * 61 + char.code - 32 }
    }
}
