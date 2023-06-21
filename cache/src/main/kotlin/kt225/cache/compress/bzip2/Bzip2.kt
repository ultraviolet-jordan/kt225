package kt225.cache.compress.bzip2

import org.openrs2.compress.bzip2.Bzip2
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * @author Jordan Abraham
 */
fun bzip2Decompress(bytes: ByteArray): ByteArray {
    val stream = ByteArrayInputStream(bytes)
    val inputStream = Bzip2.createHeaderlessInputStream(stream)
    val decompressed = inputStream.readAllBytes()
    inputStream.close()
    return decompressed
}

fun bzip2Compress(bytes: ByteArray): ByteArray {
    val stream = ByteArrayOutputStream()
    val outputStream = Bzip2.createHeaderlessOutputStream(stream)
    outputStream.write(bytes)
    outputStream.close()
    return stream.toByteArray()
}
