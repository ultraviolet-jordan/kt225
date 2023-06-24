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
    private val accumulator = JagFileAccumulator()

    val crc: Int get() = CRC32().apply { update(bytes) }.value.toInt()
    val view: Set<Map.Entry<Int, ByteArray?>> get() = accumulator.entries
    val last: Map.Entry<Int, ByteArray?>? get() = accumulator.last
    
    init {
        unpack()
    }

    fun read(name: String): ByteBuffer? {
        return accumulator.read(name.nameHash())?.value?.let(ByteBuffer::wrap)
    }

    fun add(name: String, buffer: ByteBuffer): Boolean {
        return accumulator.write(name.nameHash(), buffer.gdata())
    }

    fun remove(name: String): Boolean {
        return accumulator.remove(name.nameHash()) == null
    }
    
    fun release() {
        accumulator.clear()
    }

    fun unpack() {
        val jag = ByteBuffer.wrap(bytes)
        require(jag.remaining() >= 6) { "Invalid input byte array." }
        val decompressed = jag.g3()
        val compressed = jag.g3()
        val isCompressed = decompressed != compressed
        val bytes = when {
            isCompressed -> {
                accumulator.compressedWhole = true
                bzip2Decompress(jag.gdata(compressed))
            }
            else -> jag.gdata(jag.remaining())
        }
        if (isCompressed) {
            require(decompressed == bytes.size) { "Invalid compressed data." }
        }
        accumulator.attach(bytes)
        val entries = ByteBuffer.wrap(bytes)
        require(entries.remaining() >= 2) { "Invalid buffer length." }
        val length = entries.g2()
        entries.unpackEntries(length)
    }

    fun pack(): ByteArray {
        accumulator.readFully()
        val entries = ByteBuffer.allocate(accumulator.estimated + 10 + 6)
        val keys = accumulator.entries
        entries.p2(keys.size)
        val offset = entries.packEntries(keys)
        entries.position(offset)
        entries.flip()
        val entriesBytes = entries.gdata(offset)
        val jag = ByteBuffer.allocate(entriesBytes.size + 6)
        jag.p3(entriesBytes.size)
        val bytes = when {
            accumulator.compressedWhole -> bzip2Compress(entriesBytes)
            else -> entriesBytes
        }
        jag.p3(bytes.size)
        jag.pdata(bytes)
        jag.flip()
        return jag.gdata()
    }

    private tailrec fun ByteBuffer.unpackEntries(
        length: Int,
        index: Int = 0,
        offset: Int = 2 + length * 10
    ) {
        if (index == length) {
            return
        }
        require(remaining() >= 10) { "Invalid buffer length." }
        val nameHash = g4()
        val decompressed = g3()
        val compressed = g3()
        accumulator.alloc(nameHash, decompressed, compressed, offset)
        return unpackEntries(length, index + 1, offset + compressed)
    }

    private tailrec fun ByteBuffer.packEntries(
        keys: Set<MutableMap.MutableEntry<Int, ByteArray?>>,
        index: Int = 0,
        offset: Int = 2 + keys.size * 10
    ): Int {
        if (index == keys.size) {
            return offset
        }
        val key = keys.elementAt(index)
        val bytes = key.value ?: return packEntries(keys, index + 1, offset)
        p4(key.key)
        p3(bytes.size)
        val compressed = when {
            accumulator.compressedWhole -> bytes
            else -> bzip2Compress(bytes)
        }
        p3(compressed.size)
        mark()
        pdata(compressed, offset) // This moves the position.
        reset()
        return packEntries(keys, index + 1, offset + compressed.size)
    }

    private fun String.nameHash(): Int {
        return uppercase().fold(0) { hash, char -> hash * 61 + char.code - 32 }
    }

    private class JagFileAccumulator : MutableMap<Int, ByteArray?> by LinkedHashMap() {
        private val decompressedPointers = ArrayList<Int?>()
        private val compressedPointers = ArrayList<Int?>()
        private val positionPointers = ArrayList<Int?>()
        private var backing: ByteArray? = null
        private var released: Boolean = false

        val estimated: Int get() = values.filterNotNull().sumOf(ByteArray::size)
        val last: Map.Entry<Int, ByteArray?>? get() = read(keys.lastOrNull())
        var compressedWhole = false

        override fun remove(key: Int): ByteArray? {
            require(!released)
            val index = keys.indexOf(key)
            if (index == -1) {
                return null
            }
            require(release(index, key))
            return get(key)
        }

        override fun clear() {
            require(!released)
            entries.removeAll { true }
            decompressedPointers.removeAll { true }
            compressedPointers.removeAll { true }
            positionPointers.removeAll { true }
            backing = null
            released = true
        }

        fun read(hash: Int?): Map.Entry<Int, ByteArray?>? {
            require(!released)
            if (!checkAccumulation(hash)) {
                return null
            }
            return entries.firstOrNull { it.key == hash }
        }

        fun readFully() {
            require(!released)
            keys.forEach(::read)
        }

        fun write(hash: Int, bytes: ByteArray): Boolean {
            require(!released)
            set(hash, bytes)
            return checkAccumulation(hash)
        }

        fun attach(bytes: ByteArray) {
            require(!released)
            backing = bytes
        }

        fun alloc(hash: Int, decompressed: Int, compressed: Int, position: Int) {
            require(!released)
            set(hash, null)
            decompressedPointers.add(decompressed)
            compressedPointers.add(compressed)
            positionPointers.add(position)
        }

        private fun release(index: Int, hash: Int): Boolean {
            decompressedPointers[index] = null
            compressedPointers[index] = null
            positionPointers[index] = null
            return entries.removeIf { it.key == hash }
        }

        private fun checkAccumulation(hash: Int?): Boolean {
            if (hash == null || !containsKey(hash)) {
                return false
            }
            val accumulated = get(hash) != null
            if (!accumulated) {
                val index = keys.indexOf(hash)
                return accumulate(index, hash)
            }
            return get(hash) != null
        }

        private fun accumulate(index: Int, hash: Int): Boolean {
            if (index == -1) {
                return false
            }
            val decompressed = decompressedPointers[index] ?: return false
            val compressed = compressedPointers[index] ?: return false
            val position = positionPointers[index] ?: return false
            val bytes = if (compressedWhole) {
                backing?.copyOfRange(position, position + decompressed)
            } else {
                backing?.copyOfRange(position, position + compressed)?.let(::bzip2Decompress)
            }
            set(hash, bytes)
            return bytes != null
        }
    }
}
