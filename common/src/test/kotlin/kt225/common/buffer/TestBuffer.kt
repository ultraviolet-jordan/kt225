package kt225.common.buffer

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.util.io.pem.PemReader
import java.io.StringReader
import java.nio.ByteBuffer
import java.nio.InvalidMarkException
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.PKCS8EncodedKeySpec
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
        val result = buffer.flip.g1
        assertEquals(255, result)
    }

    @Test
    fun `test 1 signed`() {
        val buffer = ByteBuffer.allocate(1)
        buffer.p1(69)
        val result = buffer.flip.g1b
        assertEquals(69, result)
    }

    @Test
    fun `test 2`() {
        val buffer = ByteBuffer.allocate(2)
        buffer.p2(65535)
        val result = buffer.flip.g2
        assertEquals(65535, result)
    }

    @Test
    fun `test 2 LE`() {
        val buffer = ByteBuffer.allocate(2)
        buffer.ip2(65535)
        val result = buffer.flip.ig2
        assertEquals(65535, result)
    }

    @Test
    fun `test 3`() {
        val buffer = ByteBuffer.allocate(3)
        buffer.p3(696969)
        val result = buffer.flip.g3
        assertEquals(696969, result)
    }

    @Test
    fun `test 4`() {
        val buffer = ByteBuffer.allocate(4)
        buffer.p4(Int.MAX_VALUE)
        val result = buffer.flip.g4
        assertEquals(Int.MAX_VALUE, result)
    }

    @Test
    fun `test 4 LE`() {
        val buffer = ByteBuffer.allocate(4)
        buffer.ip4(Int.MAX_VALUE)
        val result = buffer.flip.ig4
        assertEquals(Int.MAX_VALUE, result)
    }

    @Test
    fun `test 8`() {
        val buffer = ByteBuffer.allocate(8)
        buffer.p8(Long.MAX_VALUE)
        val result = buffer.flip.g8
        assertEquals(Long.MAX_VALUE, result)
    }

    @Test
    fun `test smart`() {
        val byte = ByteBuffer.allocate(2)
        byte.psmart(2)
        val result = byte.flip.gsmart
        assertEquals(2, result)
    }

    @Test
    fun `test smart signed`() {
        val byte = ByteBuffer.allocate(2)
        byte.psmarts(69)
        val result = byte.flip.gsmarts
        assertEquals(69, result)
    }

    @Test
    fun `test str`() {
        val expected = "Hello this is a test string."
        val buffer = ByteBuffer.allocate(expected.length + 1)
        buffer.pstr(expected)
        val result = buffer.flip.gstr
        assertEquals(expected, result)
    }

    @Test
    fun `test j str`() {
        val expected = "Hello this is a test string."
        val buffer = ByteBuffer.allocate(expected.length + 1)
        buffer.pjstr(expected)
        val result = buffer.flip.gjstr
        assertEquals(expected, result)
    }

    @Test
    fun `test data`() {
        val expected = Random.nextBytes(255)
        val buffer = ByteBuffer.allocate(expected.size)
        buffer.pdata(expected)
        val result = buffer.flip.gdata
        assertContentEquals(expected, result)
    }

    @Test
    fun `test rsa`() {
        val rsaPrivateCrtKey = createPrivateKey()
        val buffer = ByteBuffer.allocate(65 + 1)
        buffer.p1(69)
        buffer.rsaenc(rsaPrivateCrtKey)
        buffer.rsadec(rsaPrivateCrtKey)
        val result = buffer.g1
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
        assertEquals(buffer.position, 2)
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
        assertEquals(buffer.position, 1)
        buffer.position(0)
        buffer.accessBits {
            assertEquals(0, gbit(2))
        }
    }

    private fun createPrivateKey(): RSAPrivateCrtKey {
        val pem = """
            RSA Private-Key: (512 bit, 2 primes)
            modulus:
                00:88:c3:87:48:a5:82:28:f7:26:1c:dc:34:0b:56:
                91:d7:d0:97:5d:ee:0e:cd:b7:17:60:9e:6b:f9:71:
                eb:3f:e7:23:ef:9d:13:0e:46:86:81:37:39:76:8a:
                d9:47:2e:b4:6d:8b:fc:c0:42:c1:a5:fc:b0:5e:93:
                1f:63:2e:ea:5d
            publicExponent:
                00:81:f3:90:b2:cf:8c:a7:03:9e:e5:07:97:59:51:
                d5:a0:b1:5a:87:bf:8b:3f:99:c9:66:83:41:18:c5:
                0f:d9:4d
            privateExponent:
                57:1f:b0:62:04:8b:61:72:1e:bf:cf:1e:87:71:53:
                24:1b:70:c3:aa:26:ed:b0:f9:f0:6a:1b:2b:e0:7c:
                4e:45:ea:ba:4f:c3:56:ea:80:6c:be:d2:98:d3:86:
                13:59:0a:53:fd:e0:38:3c:3a:41:17:58:51:62:93:
                24:09:25:e5
            prime1:
                00:d7:66:c4:5d:8d:54:12:48:e5:09:58:36:59:43:
                8a:d7:85:20:d0:a7:7c:7c:a6:b2:be:81:56:3a:85:
                13:6a:19
            prime2:
                00:a2:8a:74:05:f9:2c:ac:fb:62:4e:cd:4c:7c:d0:
                e5:87:47:f2:6a:70:5e:9e:a9:cd:b4:bb:b3:e7:7c:
                1d:52:e5
            exponent1:
                00:92:1c:92:74:e6:ee:1d:ce:1f:79:1b:c7:90:ea:
                02:dc:cb:de:d1:09:c8:3f:37:1f:71:0c:33:c1:91:
                4e:60:7d
            exponent2:
                5b:eb:08:e5:ad:c3:37:00:81:b4:f1:54:95:ca:1c:
                42:4d:e5:30:4b:d7:24:e4:d6:8d:57:c2:cc:2c:5c:
                75:f5
            coefficient:
                15:dc:1c:b7:a1:e8:dc:7d:a7:4c:3b:ed:87:0b:9d:
                1a:19:c4:d6:bc:1b:40:47:93:cb:d7:8e:00:c5:0c:
                49:26
            -----BEGIN PRIVATE KEY-----
            MIIBcgIBADANBgkqhkiG9w0BAQEFAASCAVwwggFYAgEAAkEAiMOHSKWCKPcmHNw0
            C1aR19CXXe4OzbcXYJ5r+XHrP+cj750TDkaGgTc5dorZRy60bYv8wELBpfywXpMf
            Yy7qXQIhAIHzkLLPjKcDnuUHl1lR1aCxWoe/iz+ZyWaDQRjFD9lNAkBXH7BiBIth
            ch6/zx6HcVMkG3DDqibtsPnwahsr4HxOReq6T8NW6oBsvtKY04YTWQpT/eA4PDpB
            F1hRYpMkCSXlAiEA12bEXY1UEkjlCVg2WUOK14Ug0Kd8fKayvoFWOoUTahkCIQCi
            inQF+Sys+2JOzUx80OWHR/JqcF6eqc20u7PnfB1S5QIhAJIcknTm7h3OH3kbx5Dq
            AtzL3tEJyD83H3EMM8GRTmB9AiBb6wjlrcM3AIG08VSVyhxCTeUwS9ck5NaNV8LM
            LFx19QIgFdwct6Ho3H2nTDvthwudGhnE1rwbQEeTy9eOAMUMSSY=
            -----END PRIVATE KEY-----
        """.trimIndent()

        val pemReader = PemReader(StringReader(pem))
        val pemObject = pemReader.readPemObject() ?: throw IllegalArgumentException("Invalid PEM data")
        pemReader.close()

        val encodedKey = PrivateKeyInfo.getInstance(pemObject.content)
        val keySpec = PKCS8EncodedKeySpec(encodedKey.encoded)
        return KeyFactory.getInstance("RSA").generatePrivate(keySpec) as RSAPrivateCrtKey
    }
}
