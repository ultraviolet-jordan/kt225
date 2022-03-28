package kt225.cache.archive

import bzip2.BZip2InputStream
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort
import kt225.shared.readUMedium
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
class DecodedArchive(
    private val src: ByteArray
) {
    private var decompressed: Boolean = false
    private var fileCount: Int = 0
    private var fileHash: IntArray = intArrayOf()
    private var fileUnpackedSize: IntArray = intArrayOf()
    private var filePackedSize: IntArray = intArrayOf()
    private var fileOffset: IntArray = intArrayOf()
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
    }

    /**
     * Reads an internal file from this decoded archive file.
     */
    fun read(name: String): ByteArray {
        val hash = name.nameHash()
        repeat(fileCount) {
            if (fileHash[it] == hash) {
                val dest = ByteArray(fileUnpackedSize[it])
                if (!decompressed) {
                    BZip2InputStream.read(dest, fileUnpackedSize[it], data, filePackedSize[it], fileOffset[it])
                } else {
                    System.arraycopy(data, fileOffset[it], dest, 0, fileUnpackedSize[it])
                }
                return dest
            }
        }
        return byteArrayOf()
    }

    private fun decodeCompression(): ByteReadPacket {
        val buffer = ByteReadPacket(src)
        require(buffer.remaining >= 6)
        val unpacked = buffer.readUMedium()
        val packed = buffer.readUMedium()
        return if (unpacked != packed) {
            data = ByteArray(unpacked).also { BZip2InputStream.read(it, unpacked, src, packed, 6) }
            decompressed = true
            ByteReadPacket(data)
        } else {
            data = src
            buffer
        }
    }

    private fun ByteReadPacket.decodeFiles() {
        require(remaining >= 2)
        fileCount = readUShort().toInt()
        require(remaining >= fileCount * 10)
        fileHash = IntArray(fileCount)
        fileUnpackedSize = IntArray(fileCount)
        filePackedSize = IntArray(fileCount)
        fileOffset = IntArray(fileCount)
        var offset = 8 + fileCount * 10
        repeat(fileCount) {
            fileHash[it] = readInt()
            fileUnpackedSize[it] = readUMedium()
            filePackedSize[it] = readUMedium()
            fileOffset[it] = offset
            offset += filePackedSize[it]
        }
    }

    private fun String.nameHash(): Int {
        var hash = 0
        val name = uppercase()
        name.indices.forEach {
            hash = hash * 61 + name[it].code - 32
        }
        return hash
    }
}
