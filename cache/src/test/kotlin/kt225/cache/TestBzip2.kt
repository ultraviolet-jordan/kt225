package kt225.cache

import kt225.cache.compress.bzip2.bzip2Compress
import kt225.cache.compress.bzip2.bzip2Decompress
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals

/**
 * @author Jordan Abraham
 */
class TestBzip2 {
    @Test
    fun `test bzip2`() {
        val random = Random.nextBytes(500)
        val x = bzip2Compress(random)
        val z = bzip2Decompress(x)
        assertContentEquals(random, z)
    }
}
