package kt225.shared

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeShort

fun BytePacketBuilder.writeStringCp1252NullTerminated(value: String) {
    value.toByteArray().forEach(::writeByte)
    writeByte(0)
}

fun BytePacketBuilder.writeBytes(bytes: ByteArray) = bytes.forEach(::writeByte)

fun BytePacketBuilder.writeMedium(value: Int) {
    writeByte((value shr 16).toByte())
    writeShort(value.toShort())
}
