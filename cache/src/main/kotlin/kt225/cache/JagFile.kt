package kt225.cache

import kt225.cache.compress.bzip2.bzip2Compress
import kt225.cache.compress.bzip2.bzip2Decompress
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
abstract class JagFile(
    val bytes: ByteArray
) {
    private val accumulator = JagFileAccumulator(bytes)
    private var compressedWhole = false
    
    val crc: Int get() {
        val crc32 = CRC32()
        crc32.update(bytes)
        return crc32.value.toInt()
    }
    
    val list: Map<Int, ByteArray?> get() = accumulator.list()
    val last: Map.Entry<Int, ByteArray?>? = accumulator.last()

    fun read(name: String): ByteBuffer? {
        val entry = accumulator.read(name.nameHash()) ?: return null
        return entry.value?.let(ByteBuffer::wrap)
    }

    fun add(name: String, buffer: ByteBuffer): Boolean {
        val bytes = buffer.gdata()
        accumulator.write(name.nameHash(), bytes)
        return true
    }

    fun remove(name: String): Boolean {
        return accumulator.remove(name.nameHash())
    }

    inline fun <reified T : JagFile> unpack(): T {
        val jag = ByteBuffer.wrap(bytes)
        require(jag.remaining() >= 6) { "Invalid input byte array." }
        val decompressed = jag.g3()
        val compressed = jag.g3()
        val isCompressed = decompressed != compressed
        val entries = when {
            isCompressed -> ByteBuffer.wrap(bzip2Decompress(jag.gdata(compressed)))
            else -> jag
        }
        if (isCompressed) {
            require(decompressed == entries.capacity()) { "Invalid compressed data." }
        }
        require(entries.remaining() >= 2) { "Invalid buffer length." }
        val length = entries.g2()
        entries.unpackEntries(length, isCompressed)
        return this as T
    }

    fun pack(): ByteArray {
        accumulator.readFully()
        val entries = ByteBuffer.allocate(accumulator.allocationEstimate + 8 + 2)
        val keys = accumulator.keys()
        entries.p2(keys.size)
        val offset = entries.packEntries(keys, compressedWhole)
        entries.position(offset)
        entries.flip()
        val entriesBytes = entries.gdata(offset)
        val jag = ByteBuffer.allocate(entriesBytes.size + 6)
        jag.p3(entriesBytes.size)
        val bytes = when {
            compressedWhole -> bzip2Compress(entriesBytes)
            else -> entriesBytes
        }
        jag.p3(bytes.size)
        jag.pdata(bytes)
        jag.flip()
        return jag.gdata()
    }

    tailrec fun ByteBuffer.unpackEntries(
        length: Int,
        isCompressed: Boolean,
        id: Int = 0,
        offset: Int = 8 + length * 10
    ) {
        if (id == length) {
            return
        }
        require(remaining() >= 10) { "Invalid buffer length." }
        val nameHash = g4()
        val decompressed = g3()
        val compressed = g3()
        accumulator.collect(nameHash, decompressed, compressed, offset)
        return unpackEntries(length, isCompressed, id + 1, offset + compressed)
    }

    private tailrec fun ByteBuffer.packEntries(
        keys: Set<Int>,
        isCompressed: Boolean,
        index: Int = 0,
        offset: Int = 2 + accumulator.size * 10
    ): Int {
        if (index == accumulator.size) {
            return offset
        }
        val key = keys.elementAt(index)
        val entry = accumulator.read(key) ?: return packEntries(keys, isCompressed, index + 1, offset)
        val bytes = entry.value ?: return packEntries(keys, isCompressed, index + 1, offset)
        p4(key)
        p3(bytes.size)
        val compressed = when {
            isCompressed -> bytes
            else -> bzip2Compress(bytes)
        }
        p3(compressed.size)
        mark()
        pdata(compressed, offset) // This moves the position.
        reset()
        return packEntries(keys, isCompressed, index + 1, offset + compressed.size)
    }

    private fun String.nameHash(): Int {
        return uppercase().fold(0) { hash, char -> hash * 61 + char.code - 32 }
    }
}
