package kt225.cache.archive

import io.ktor.utils.io.core.ByteReadPacket
import kt225.cache.cacheResource
import kt225.shared.readUMedium
import java.io.DataInputStream
import java.io.InputStream

/**
 * @author Jordan Abraham
 */
internal object Config : Archive(cacheResource("config"))

internal open class Archive(
    private val src: InputStream
) {
    /**
     * The decoded archive file.
     */
    val archive by lazy(::decodeArchive)

    /**
     * Decodes an archive file from the raw stream to our DecodedArchive.
     */
    private fun decodeArchive(): DecodedArchive {
        require(src.available() > 0)
        val stream = DataInputStream(src)
        val header = stream.decodeHeader()
        val buffer = ByteReadPacket(header).also { it.discard(3) }
        val size = buffer.readUMedium() + 6
        buffer.release()
        return DecodedArchive(stream.decode(header, size).also { stream.close() })
    }

    /**
     * Decodes the file header.
     */
    private fun DataInputStream.decodeHeader(): ByteArray {
        require(available() >= 6)
        val header = ByteArray(6)
        readFully(header, 0, 6)
        return header
    }

    /**
     * Decodes the rest of the file contents.
     */
    private fun DataInputStream.decode(header: ByteArray, size: Int): ByteArray {
        require(header.size == 6)
        require(size > 0)
        // No loop required. =)
        return decodeRecursively(
            data = header.copyInto(ByteArray(size)),
            offset = header.size,
            size = size
        )
    }

    private tailrec fun DataInputStream.decodeRecursively(data: ByteArray, offset: Int, size: Int): ByteArray {
        if (offset >= size) return data
        val remaining = (size - offset).let { if (it > 1000) 1000 else it }
        val nextOffset = offset + read(data, offset, remaining)
        return decodeRecursively(
            data = data,
            offset = nextOffset,
            size = size
        )
    }
}
