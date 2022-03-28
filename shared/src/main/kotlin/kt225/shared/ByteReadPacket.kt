package kt225.shared

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
fun ByteReadPacket.readStringCp1252NullTerminated() = buildString {
    var char: Char
    while (readByte().toInt().also { char = it.toChar() } != 10) append(char)
}

fun ByteReadPacket.readUShortLittleEndian() = readUByte().toInt() or (readUByte().toInt() shl 8)

fun ByteReadPacket.readUMedium() = (readUByte().toInt() shl 16) or readUShort().toInt()
