package kt225.common.buffer

import java.nio.ByteBuffer
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals

/**
 * @author Jordan Abraham
 */
class BufferTest {
    @Test
    fun `test bzip2`() {
        val random = Random.nextBytes(500)
        val x = bzip2Compress(random)
        val z = ByteBuffer.wrap(x).bzip2Decompress()
        assertContentEquals(random, z)
    }
}
