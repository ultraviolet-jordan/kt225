package kt225.cache.archive

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort
import kt225.cache.decompressBzip2
import kt225.shared.readUMedium
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
internal class DecodedArchive(
    private val src: ByteArray
) {
    private var decompressed: Boolean = false
    private var hashes: IntArray = intArrayOf()
    private var unpackedSizes: IntArray = intArrayOf()
    private var packedSizes: IntArray = intArrayOf()
    private var offsets: IntArray = intArrayOf()
    private var crc32: CRC32 = CRC32()
    private var data: ByteArray = byteArrayOf()

    init {
        // In this order.
        require(src.isNotEmpty())
        val buffer = decodeCompression()
        require(data.isNotEmpty())
        crc32.reset()
        crc32.update(data)
        buffer.decodeFiles()
        buffer.release()
    }

    /**
     * Reads an internal file from this decoded archive file.
     */
    fun read(name: String): ByteArray {
        // Don't have to loop here like the client does. =)
        val index = hashes.indexOf(name.archiveHash())
        if (index == -1) return byteArrayOf()
        return ByteArray(unpackedSizes[index]).also {
            when {
                // If not decompressed yet, decompress with bzip2 here.
                !decompressed -> data.decompressBzip2(it, unpackedSizes[index], packedSizes[index], offsets[index])
                // If we are already decompressed then just copy.
                else -> data.copyInto(it, 0, offsets[index], unpackedSizes[index])
            }
        }
    }

    fun crc() = crc32.value.toInt()
    fun bytes() = src.clone()

    /**
     * Checks if this archive file needs to be decompressed then decompresses if so.
     */
    private fun decodeCompression(): ByteReadPacket {
        val buffer = ByteReadPacket(src)
        require(buffer.remaining >= 6)
        val unpacked = buffer.readUMedium()
        val packed = buffer.readUMedium()
        return if (unpacked != packed) {
            // Decompress with bzip2 then return new wrapped buffer.
            data = ByteArray(unpacked).also { src.decompressBzip2(it, unpacked, packed, 6) }
            decompressed = true
            ByteReadPacket(data)
        } else {
            // Just use this buffer since we don't need to decompress.
            data = src
            buffer
        }
    }

    /**
     * Decodes through the archive and sets up the file pointer arrays.
     */
    private fun ByteReadPacket.decodeFiles() {
        require(remaining >= 2)
        val size = readUShort().toInt()
        require(remaining >= size * 10)
        hashes = IntArray(size)
        unpackedSizes = IntArray(size)
        packedSizes = IntArray(size)
        offsets = IntArray(size)
        var offset = 8 + size * 10
        repeat(size) {
            hashes[it] = readInt()
            unpackedSizes[it] = readUMedium()
            packedSizes[it] = readUMedium()
            offsets[it] = offset
            offset += packedSizes[it]
        }
    }

    private fun String.archiveHash() = uppercase().fold(0) { hash, char -> hash * 61 + char.code - 32 }
}
