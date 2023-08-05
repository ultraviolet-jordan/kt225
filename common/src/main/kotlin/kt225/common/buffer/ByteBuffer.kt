package kt225.common.buffer

import java.math.BigInteger
import java.nio.ByteBuffer
import java.security.interfaces.RSAPrivateCrtKey
import kotlin.math.min

/**
 * @author Jordan Abraham
 */

/**
 * Returns the number of elements between the current position and the limit.
 */
inline val ByteBuffer.remaining: Int
    get() = remaining()

/**
 * Tells whether there are any elements between the current position and the limit.
 */
inline val ByteBuffer.hasRemaining: Boolean
    get() = hasRemaining()

/**
 * Returns this buffer's position.
 */
inline val ByteBuffer.position: Int
    get() = position()

/**
 * Returns this buffer's limit.
 */
inline val ByteBuffer.limit: Int
    get() = limit()

/**
 * Flips this buffer. 
 * The limit is set to the current position and then the position is set to zero. 
 * If the mark is defined then it is discarded.
 */
inline val ByteBuffer.flip: ByteBuffer
    get() = flip()

/**
 * Returns the unsigned byte at the current position.
 * Does not move the position.
 */
inline val ByteBuffer.peek: Int
    get() = peekb and 0xff

/**
 * Returns the signed byte at the current position.
 * Does not move the position.
 */
inline val ByteBuffer.peekb: Int
    get() = this[position].toInt()

/**
 * Get 1 byte from this ByteBuffer.
 */
inline val ByteBuffer.g1: Int 
    get() = get().toInt() and 0xff

/**
 * Get 1 signed byte from this ByteBuffer.
 */
inline val ByteBuffer.g1b: Int 
    get() = get().toInt()

/**
 * Get 2 bytes from this ByteBuffer.
 */
inline val ByteBuffer.g2: Int 
    get() = getShort().toInt() and 0xffff

/**
 * Get 2 signed bytes from this ByteBuffer.
 */
inline val ByteBuffer.g2b: Int 
    get() = getShort().toInt()

/**
 * Get 2 bytes from this ByteBuffer LE.
 */
inline val ByteBuffer.ig2: Int 
    get() = (get().toInt() and 0xff) or (get().toInt() and 0xff shl 8)

/**
 * Get 3 bytes from this ByteBuffer.
 */
inline val ByteBuffer.g3: Int 
    get() = (get().toInt() and 0xff shl 16) or (getShort().toInt() and 0xffff)

/**
 * Get 4 bytes from this ByteBuffer.
 */
inline val ByteBuffer.g4: Int 
    get() = getInt()

/**
 * Get 4 bytes from this ByteBuffer LE.
 */
inline val ByteBuffer.ig4: Int
    get() = (get().toInt() and 0xff) or (get().toInt() and 0xff shl 8) or (get().toInt() and 0xff shl 16) or (get().toInt() and 0xff shl 24)

/**
 * Get 8 bytes from this ByteBuffer.
 */
inline val ByteBuffer.g8: Long 
    get() = getLong()

/**
 * Get 1 byte from this ByteBuffer if the next byte is < 128.
 * Get 2 bytes from this ByteBuffer if the next byte is >= 128 and < 65535
 */
inline val ByteBuffer.gsmarts: Int
    get() = if (peek < 128) g1 else g2 - 32768

/**
 * Get 1 byte from this ByteBuffer if the next byte is < 128.
 * Get 2 bytes from this ByteBuffer if the next byte is >= 128 and < 65535
 */
inline val ByteBuffer.gsmart: Int 
    get() = if (peek < 128) g1 - 64 else g2 - 49152

/**
 * Get a string from this ByteBuffer.
 * Moves the buffer position from the current position + string.length + 1
 */
inline val ByteBuffer.gstr: String 
    get() = String(gdata(lengthToByte(10))).also {
        pad(1)
    }

/**
 * Get bytes from this ByteBuffer.
 * The position of this buffer is moved to the current position + the length.
 */
inline val ByteBuffer.gdata: ByteArray
    get() = gdata()

/**
 * Get bytes from this ByteBuffer.
 * The position of this buffer is moved to the current position + the length.
 */
fun ByteBuffer.gdata(size: Int = limit(), position: Int = position(), length: Int = size): ByteArray {
    return ByteArray(size).also {
        get(position, it, 0, length) // Doesn't move position.
        pad(length)
    }
}

/**
 * Directly decrypt this ByteBuffer with RSA.
 * The position is set to 0 after this function is called.
 */
fun ByteBuffer.rsadec(pem: RSAPrivateCrtKey) {
    val p = pem.primeP
    val q = pem.primeQ
    val dP = pem.primeExponentP
    val dQ = pem.primeExponentQ
    val qInv = pem.crtCoefficient

    val data = BigInteger(1, gdata(g1))
    val m1 = data.mod(p).modPow(dP, p)
    val m2 = data.mod(q).modPow(dQ, q)

    val h = qInv * (m1 - m2) % p

    position(0)
    pdata((m2 + h * q).toByteArray())
    position(0)
}

/**
 * Gets a specified number of bits from this ByteBuffer.
 * This function must only be called from [ByteBuffer.accessBits]
 */
fun ByteBuffer.gbit(count: Int): Int {
    val position = this.position
    // Constantly mark and reset.
    reset()
    val marked = this.position
    // Keeps the mark positioned at the starting byte index.
    mark()
    val index = marked + (position - marked)
    val value = gbit(count, index shr 3, index and 7, 0)
    val nextPosition = position + count
    require(nextPosition <= limit) { "Buffer does not have enough capacity for byte -> bit positioning." }
    position(nextPosition)
    return value
}

/**
 * Puts 1 byte into this ByteBuffer.
 */
fun ByteBuffer.p1(value: Int) {
    put(value.toByte())
}

/**
 * Puts 2 bytes into this ByteBuffer.
 */
fun ByteBuffer.p2(value: Int) {
    putShort(value.toShort())
}

/**
 * Puts 2 bytes into this ByteBuffer LE.
 */
fun ByteBuffer.ip2(value: Int) {
    put(value.toByte())
    put((value shr 8).toByte())
}

/**
 * Puts 3 bytes into this ByteBuffer.
 */
fun ByteBuffer.p3(value: Int) {
    put((value shr 16).toByte())
    putShort(value.toShort())
}

/**
 * Puts 4 bytes into this ByteBuffer.
 */
fun ByteBuffer.p4(value: Int) {
    putInt(value)
}

/**
 * Puts 4 bytes into this ByteBuffer LE.
 */
fun ByteBuffer.ip4(value: Int) {
    put(value.toByte())
    put((value shr 8).toByte())
    put((value shr 16).toByte())
    put((value shr 24).toByte())
}

/**
 * Puts 8 bytes into this ByteBuffer.
 */
fun ByteBuffer.p8(value: Long) {
    putLong(value)
}

/**
 * Puts 1 byte into this ByteBuffer if the value is < 128.
 * Puts 2 bytes into this ByteBuffer if the value is >= 128 and < 65535
 */
fun ByteBuffer.psmarts(value: Int) {
    if (value >= 128) {
        putShort((value + 32768).toShort())
    } else {
        put(value.toByte())
    }
}

/**
 * Puts 1 byte into this ByteBuffer if the value is < 128.
 * Puts 2 bytes into this ByteBuffer if the value is >= 128 and < 65535
 */
fun ByteBuffer.psmart(value: Int) {
    if (value >= 128) {
        putShort((value + 49152).toShort())
    } else {
        put((value + 64).toByte())
    }
}

/**
 * Put a string into this ByteBuffer.
 * Moves the buffer position from the current position + string.length + 1
 */
fun ByteBuffer.pjstr(value: String) {
    put(value.toByteArray())
    put(10)
}

/**
 * Put bytes into this ByteBuffer.
 * The position of this buffer is moved to the current position + the length.
 */
fun ByteBuffer.pdata(bytes: ByteArray, position: Int = position(), length: Int = bytes.size) {
    put(position, bytes, 0, length) // Doesn't move position.
    pad(length)
}

fun ByteBuffer.psize1(value: Int) {
    put(position - value - 1, value.toByte())
}

fun ByteBuffer.psize2(value: Int) {
//    putShort(position - value - 2, value.toShort())
    put(position - value - 2, (value shr 8).toByte())
    put(position - value - 1, value.toByte())
}

/**
 * Directly encrypt this ByteBuffer with RSA.
 * The position is set to the length of the encrypted bytes.
 * It is up to the caller of this function to flip the buffer for reading.
 */
fun ByteBuffer.rsaenc(pem: RSAPrivateCrtKey) {
    val length = position
    position(0)
    // raw rsa encryption.
    val enc = BigInteger(1, gdata(length)).modPow(pem.publicExponent, pem.modulus)
    position(0)
    p1((enc.bitLength() + 7 ushr 3) + 1)
    pdata(enc.toByteArray())
    position(0)
}

/**
 * Puts a specified number of bits from the given value to this ByteBuffer.
 * This function must only be called from [ByteBuffer.accessBits]
 */
fun ByteBuffer.pbit(count: Int, value: Int) {
    val position = this.position
    // Constantly mark and reset.
    reset()
    val marked = this.position
    // Keeps the mark positioned at the starting byte index.
    mark()
    val index = marked + (position - marked)
    pbit(value, count, index shr 3, index and 7)
    val nextPosition = position + count
    require(nextPosition <= limit) { "Buffer does not have enough capacity for byte -> bit positioning." }
    position(nextPosition)
}

fun ByteBuffer.pad(amount: Int) {
    require(amount >= 0)
    position(position() + amount)
}

tailrec fun ByteBuffer.lengthToByte(terminator: Int, length: Int = 0): Int {
    if (this[position + length].toInt() == terminator) {
        return length
    }
    return lengthToByte(terminator, length + 1)
}

/**
 * Gives access to bits of this ByteBuffer.
 * The ByteBuffer is properly repositioned for byte access after function invocation.
 * Note that accessing bits assumes that this ByteBuffer will have enough capacity.
 * Because of the magic position, there is a chance that this function will throw
 * if the ByteBuffer tries to newPosition > limit.
 *
 * The ByteBuffer utilizes byte position to keep track of the bits positioning.
 * If the requested number of bits is 13, then the ByteBuffer requires at least 13 bytes of limit
 * to be able to properly calculate the bits positioning.
 *
 * This function can only be used once at a time per ByteBuffer object.
 * This function marks the ByteBuffer at the last accessed bit position. This mark does not matter
 * for future invocations of this function.
 */
inline fun ByteBuffer.accessBits(function: ByteBuffer.() -> Unit) {
    position(position() shl 3)
    // The mark. The all powerful. Keeps track of the current byte index.
    // Avoids total object creation annihilation since player/npc update uses this every tick.
    // Contemporary solutions use some type of helper "BitAccess" class to wrap the buffer for bit accessing.
    // Some of these solutions also instantiate a ByteArray(4026) for writing bits??
    mark()
    function.invoke(this)
    accessBytes()
}

/**
 * This function will throw if accessed outside of [ByteBuffer.accessBits]
 * Use [ByteBuffer.accessBits] which will automatically accessBytes after invocation.
 */
fun ByteBuffer.accessBytes() {
    val position = this.position
    reset()
    // The marked starting byte index to calculate from the ending position.
    val marked = this.position
    val index = marked + (position - marked)
    position(index + 7 ushr 3)
}

private tailrec fun ByteBuffer.pbit(value: Int, remainingBits: Int, byteIndex: Int, bitIndex: Int) {
    if (remainingBits == 0) {
        return
    }
    val bitOffset = 8 - bitIndex
    // The maximum number of bits that can be written to the current byte.
    val bitsToWrite = min(remainingBits, bitOffset)
    val max = (1 shl bitsToWrite) - 1
    // The relevant bits from the value.
    val byteValue = (value ushr (remainingBits - bitsToWrite)) and max
    // The current byte with the new bits.
    put(byteIndex, (get(byteIndex).toInt() and (max shl (bitOffset - bitsToWrite)).inv() or (byteValue shl (bitOffset - bitsToWrite))).toByte())
    return pbit(
        value = value,
        remainingBits = remainingBits - bitsToWrite,
        byteIndex = byteIndex + 1,
        bitIndex = 0
    )
}

private tailrec fun ByteBuffer.gbit(remainingBits: Int, byteIndex: Int, bitIndex: Int, accumulator: Int): Int {
    if (remainingBits == 0) {
        return accumulator
    }
    val bitOffset = 8 - bitIndex
    // The maximum number of bits that can be read from the current byte.
    val bitsToRead = min(remainingBits, bitOffset)
    // The relevant bits from the current byte.
    return gbit(
        remainingBits = remainingBits - bitsToRead,
        byteIndex = byteIndex + 1,
        bitIndex = 0,
        accumulator = (accumulator shl bitsToRead) or ((get(byteIndex).toInt() ushr (bitOffset - bitsToRead)) and ((1 shl bitsToRead) - 1))
    )
}
