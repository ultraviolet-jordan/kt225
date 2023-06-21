package kt225.cache225.map

import kt225.cache.EntryProvider
import kt225.cache.EntryType
import kt225.cache.bzip2.bzip2Compress
import kt225.cache.bzip2.bzip2Decompress
import kt225.common.buffer.g4
import kt225.common.buffer.gdata
import kt225.common.buffer.p4
import kt225.common.buffer.pdata
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
interface MapSquaresProvider<E : EntryType, T : MutableMap<Int, E>> : EntryProvider<E, T> {
    fun ByteBuffer.decompress(): ByteBuffer {
        val decompressed = g4()
        val buffer = ByteBuffer.wrap(bzip2Decompress(gdata(limit() - 4)))
        require(decompressed == buffer.limit())
        return buffer
    }

    fun ByteBuffer.compress(): ByteArray {
        val bytes = gdata()
        val compressed = bzip2Compress(bytes)
        val buffer = ByteBuffer.allocate(compressed.size + 4)
        buffer.p4(bytes.size)
        buffer.pdata(compressed)
        buffer.flip()
        return buffer.gdata()
    }
}
