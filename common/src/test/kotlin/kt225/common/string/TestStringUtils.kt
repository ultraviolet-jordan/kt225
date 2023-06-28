package kt225.common.string

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Jordan Abraham
 */
class TestStringUtils {
    @Test
    fun `test base37`() {
        val string = "hello_world"
        val long = string.toBase37
        val result = long.fromBase37
        assertEquals(string, result)
    }
}
