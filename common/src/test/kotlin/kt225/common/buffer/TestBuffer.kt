package kt225.common.buffer

import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.InvalidMarkException
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * @author Jordan Abraham
 */
class TestBuffer {
    @Test
    fun `test 1`() {
        val buffer = ByteBuffer.allocate(1)
        buffer.p1(255)
        buffer.flip()
        val result = buffer.g1()
        assertEquals(255, result)
    }

    @Test
    fun `test 1 signed`() {
        val buffer = ByteBuffer.allocate(1)
        buffer.p1(69)
        buffer.flip()
        val result = buffer.g1b()
        assertEquals(69, result)
    }

    @Test
    fun `test 2`() {
        val buffer = ByteBuffer.allocate(2)
        buffer.p2(65535)
        buffer.flip()
        val result = buffer.g2()
        assertEquals(65535, result)
    }

    @Test
    fun `test 2 LE`() {
        val buffer = ByteBuffer.allocate(2)
        buffer.ip2(65535)
        buffer.flip()
        val result = buffer.ig2()
        assertEquals(65535, result)
    }

    @Test
    fun `test 3`() {
        val buffer = ByteBuffer.allocate(3)
        buffer.p3(696969)
        buffer.flip()
        val result = buffer.g3()
        assertEquals(696969, result)
    }

    @Test
    fun `test 4`() {
        val buffer = ByteBuffer.allocate(4)
        buffer.p4(Int.MAX_VALUE)
        buffer.flip()
        val result = buffer.g4()
        assertEquals(Int.MAX_VALUE, result)
    }

    @Test
    fun `test 8`() {
        val buffer = ByteBuffer.allocate(8)
        buffer.p8(Long.MAX_VALUE)
        buffer.flip()
        val result = buffer.g8()
        assertEquals(Long.MAX_VALUE, result)
    }

    @Test
    fun `test smart`() {
        val byte = ByteBuffer.allocate(2)
        byte.psmart(2)
        byte.flip()
        val result = byte.gsmart()
        assertEquals(2, result)
    }

    @Test
    fun `test smart signed`() {
        val byte = ByteBuffer.allocate(2)
        byte.psmarts(69)
        byte.flip()
        val result = byte.gsmarts()
        assertEquals(69, result)
    }

    @Test
    fun `test string`() {
        val expected = "Hello this is a test string."
        val buffer = ByteBuffer.allocate(expected.length + 1)
        buffer.pjstr(expected)
        buffer.flip()
        val result = buffer.gstr()
        assertEquals(expected, result)
    }

    @Test
    fun `test data`() {
        val expected = Random.nextBytes(255)
        val buffer = ByteBuffer.allocate(expected.size)
        buffer.pdata(expected)
        buffer.flip()
        val result = buffer.gdata()
        assertContentEquals(expected, result)
    }

    @Test
    fun `test rsa`() {
        val public = "58778699976184461502525193738213253649000149147835990136706041084440742975821"
        val private = "4563042879983685819415859508309337987464904274730456483940553788384065737798175536144539635545496149193181089921240252410947054964044522362195913220892133"
        val modulus = "7162900525229798032761816791230527296329313291232324290237849263501208207972894053929065636522363163621000728841182238772712427862772219676577293600221789"
        val buffer = ByteBuffer.allocate(65 + 1)
        buffer.p1(69)
        buffer.rsaenc(BigInteger(public), BigInteger(modulus))
        buffer.flip()
        buffer.rsadec(BigInteger(private), BigInteger(modulus))
        val result = buffer.g1()
        assertEquals(69, result)
    }

    @Test
    fun `test bits`() {
        val numberOfBits = 9
        val buffer = ByteBuffer.allocate(numberOfBits)
        buffer.accessBits {
            pbit(1, 0)
            pbit(1, 3)
            pbit(7, 13)
        }
        // Only 2 bytes required for 9 bits.
        assertEquals(buffer.position(), 2)
        buffer.position(0)
        buffer.accessBits {
            val x = gbit(1)
            val y = gbit(1)
            val z = gbit(7)
            assertEquals(0, x)
            assertEquals(1, y)
            assertEquals(13, z)
        }
    }

    @Test
    fun `test bits without access`() {
        val buffer = ByteBuffer.allocate(1)
        assertFailsWith<InvalidMarkException>(
            block = { buffer.pbit(1, 0) }
        )
    }

    @Test
    fun `test access bytes without access`() {
        val buffer = ByteBuffer.allocate(0)
        assertFailsWith<InvalidMarkException>(
            block = buffer::accessBytes
        )
    }

    @Test
    fun `test bits allocation`() {
        val illegalArgumentException = assertFailsWith<IllegalArgumentException>(
            block = {
                val buffer = ByteBuffer.allocate(1)
                buffer.accessBits {
                    buffer.pbit(2, 0)
                }
            }
        )
        assertEquals(illegalArgumentException.message, "Buffer does not have enough capacity for byte -> bit positioning.")

        val buffer = ByteBuffer.allocate(2)
        buffer.accessBits {
            buffer.pbit(2, 0)
        }
        // Only 1 byte required for 2 bits.
        assertEquals(buffer.position(), 1)
        buffer.position(0)
        buffer.accessBits {
            assertEquals(0, gbit(2))
        }
    }
}
