package kt225.cache

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class TestCRC32 {

    @Test
    fun `test crc32`() {
        val injector = Guice.createInjector(CacheModule())

        val cache = injector.getInstance<Cache>()

        val crcs = cache.crcs()

        val expected = intArrayOf(
            0,
            -430779560,
            511217062,
            1614084464,
            -343404987,
            -2000991154,
            1703545114,
            1570981179,
            -1532605973
        )

        repeat(crcs.size) {
            assertEquals(expected[it], crcs[it], "Cache archive crc at index $it does not equal ${expected[it]}.")
        }
    }
}
