package kt225.cache.archive

import io.ktor.utils.io.core.ByteReadPacket
import kt225.cache.archiveResource
import kt225.shared.readUMedium
import java.io.DataInputStream
import java.io.InputStream

/**
 * @author Jordan Abraham
 */
internal object ConfigArchive : Archive(archiveResource("config"))
internal object InterfaceArchive : Archive(archiveResource("interface"))
internal object MediaArchive : Archive(archiveResource("media"))
internal object ModelsArchive : Archive(archiveResource("models"))
internal object SoundsArchive : Archive(archiveResource("sounds"))
internal object TexturesArchive : Archive(archiveResource("textures"))
internal object TitleArchive : Archive(archiveResource("title"))
internal object WordEncArchive : Archive(archiveResource("wordenc"))

internal typealias Config = ConfigArchive
internal typealias Interface = InterfaceArchive
internal typealias Media = MediaArchive
internal typealias Models = ModelsArchive
internal typealias Sounds = SoundsArchive
internal typealias Textures = TexturesArchive
internal typealias Title = TitleArchive
internal typealias WordEnc = WordEncArchive

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
        return DecodedArchive(
            stream.decode(
                data = header.copyInto(ByteArray(size)),
                offset = header.size,
                size = size
            ).also { stream.close() }
        )
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
    private tailrec fun DataInputStream.decode(data: ByteArray, offset: Int, size: Int): ByteArray = when {
        offset >= size -> data
        else -> {
            val remaining = (size - offset).let { if (it > 1000) 1000 else it }
            val nextOffset = offset + read(data, offset, remaining)
            decode(data, nextOffset, size)
        }
    }
}
