package kt225.game.client.io

import com.runetopic.cryptography.isaac.ISAAC
import com.runetopic.cryptography.toISAAC
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.readPacket
import kt225.cache.crcs
import kt225.cache.maps
import kt225.game.client.Client
import kt225.game.client.io.ClientResponseOpcode.BAD_SESSION_OPCODE
import kt225.game.client.io.ClientResponseOpcode.CLIENT_OUTDATED_OPCODE
import kt225.game.client.io.ClientResponseOpcode.LOGIN_SUCCESS_OPCODE
import kt225.shared.readStringCp1252NullTerminated
import kt225.shared.withBitAccess
import kt225.shared.writeBytes
import java.math.BigInteger
import java.util.zip.CRC32

/**
 * @author Jordan Abraham
 */
private suspend fun Client.writeResponse(response: Int) = writeChannel.apply { writeByte(response.toByte()) }.flush()

suspend fun Client.readLogin() {
    writeChannel.apply {
        writeLong(seed)
    }.flush()

    val opcode = readChannel.readByte().toInt() and 0xff
    if (opcode != 16 && opcode != 18) {
        writeResponse(BAD_SESSION_OPCODE)
        return disconnect()
    }

    val size = readChannel.readByte().toInt() and 0xff
    val revision = readChannel.readByte().toInt() and 0xff
    if (revision != 225) {
        writeResponse(CLIENT_OUTDATED_OPCODE)
        return disconnect()
    }
    val properties = readChannel.readByte().toInt() and 0xff
    val logincrcs = IntArray(9) { readChannel.readInt() }
    if (!logincrcs.contentEquals(crcs)) {
        writeResponse(CLIENT_OUTDATED_OPCODE)
        return disconnect()
    }

    val rsa = ByteArray(readChannel.readByte().toInt() and 0xff)
    if (readChannel.readAvailable(rsa, 0, rsa.size) != rsa.size) {
        writeResponse(BAD_SESSION_OPCODE)
        return disconnect()
    }

    val exponent = "4563042879983685819415859508309337987464904274730456483940553788384065737798175536144539635545496149193181089921240252410947054964044522362195913220892133"
    val modulus = "7162900525229798032761816791230527296329313291232324290237849263501208207972894053929065636522363163621000728841182238772712427862772219676577293600221789"
    val rsaBlock = ByteReadPacket(BigInteger(rsa).modPow(BigInteger(exponent), BigInteger(modulus)).toByteArray())

    if (rsaBlock.readUByte().toInt() != 10) {
        writeResponse(BAD_SESSION_OPCODE)
        return disconnect()
    }

    val clientKeys = IntArray(4) { rsaBlock.readInt() }
    val serverKeys = IntArray(clientKeys.size) { clientKeys[it] + 50 }
    val uid = rsaBlock.readInt()
    val username = rsaBlock.readStringCp1252NullTerminated()
    val password = rsaBlock.readStringCp1252NullTerminated()

    require(rsaBlock.remaining == 0L)
    require(readChannel.availableForRead == 0)

    writeLogin(clientKeys.toISAAC(), serverKeys.toISAAC())
}

val x = 3222
val z = 3222
val zoneX = x shr 3
val zoneZ = z shr 3

private suspend fun Client.writeLogin(clientCipher: ISAAC, serverCipher: ISAAC) {
    writeResponse(LOGIN_SUCCESS_OPCODE)

    val load_area_buffer = buildPacket {
        writeShort(zoneX.toShort())
        writeShort(zoneZ.toShort())

        ((zoneX - 6) / 8..(zoneX + 6) / 8).forEach { xOffset ->
            ((zoneZ - 6) / 8..(zoneZ + 6) / 8).forEach { yOffset ->
                writeByte(xOffset.toByte())
                writeByte(yOffset.toByte())
                val crc1 = CRC32()
                crc1.reset()
                crc1.update(maps["m${xOffset}_$yOffset"]!!)
                writeInt(crc1.value.toInt())
                val crc2 = CRC32()
                crc2.reset()
                crc2.update(maps["l${xOffset}_$yOffset"]!!)
                writeInt(crc2.value.toInt())
            }
        }
    }

    writeChannel.apply {
        writeByte((0xff and 237 + serverCipher.getNext()).toByte())
        writeShort(load_area_buffer.remaining.toShort())
        writePacket(load_area_buffer)
    }.flush()

    readPackets(clientCipher, serverCipher)
}

val sizes = intArrayOf(
    0, 0, 2, 0, -1, 0, 6, 4, 2, 2,
    0, 8, 0, 0, 0, 0, 0, 1, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 2, 0, 0,
    3, 6, 0, 0, 0, 0, 0, 0, 6, 0,
    6, 0, 0, 0, 0, 0, 0, 0, 8, 0,
    0, 0, 13, 2, 0, 0, 0, 0, 0, 6,
    0, 0, 0, 0, 0, 0, 4, 0, 0, 0,
    0, 6, 0, 0, 0, 6, 0, 0, 0, 8,
    0, -1, 0, 0, 0, 0, 0, 0, 4, 0,
    0, 0, 0, -1, 0, 0, 6, 6, 0, 0,
    2, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 2, 0, 0, 6, 0, 8, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    12, 0, 0, 6, 4, 0, 0, 0, 8, 0,
    6, 0, 0, 0, 0, 0, -1, 0, 9, 0,
    3, 0, 0, 0, 0, 2, 0, 6, 3, 6,
    0, 0, 0, 0, 2, -1, 0, 0, 0, 0,
    0, 8, 6, 0, 0, 1, 2, 4, 6, 0,
    0, -1, 0, 0, 0, 2, 0, 0, 0, 6,
    10, 0, 0, 0, 2, 6, 0, 0, 0, 0,
    6, 0, 8, 0, 0, 0, 2, 0, 0, 0,
    0, 6, 6, 0, 0, 3, 0, 0, 0, -1,
    6, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 1, 0, 4, 4, 4, 1, 12,
    0, 0, 0, 0, 3, 6, 0, 6, 8, 0,
    0, 0, 0, 0, 0, 0
)

private suspend fun Client.readPackets(clientCipher: ISAAC, serverCipher: ISAAC) {
    while (true) {
        val opcode = (0xff and (readChannel.readByte().toInt() and 0xff) - clientCipher.getNext())
        if (opcode > 248) continue
        val available = readChannel.availableForRead
        val size = when (val size = sizes[opcode]) {
            -1 -> if (available >= 1) readChannel.readByte().toInt() and 0xff else available
            -2 -> if (available >= 2) readChannel.readShort().toInt() and 0xffff else available
            else -> size
        }
        readChannel.readPacket(size) // Just take it like this.
        if (opcode == 150) {
            ((zoneX - 6) / 8..(zoneX + 6) / 8).forEach { xOffset ->
                ((zoneZ - 6) / 8..(zoneZ + 6) / 8).forEach { yOffset ->
                    // data_land
                    val data_land_buffer = buildPacket {
                        writeByte(xOffset.toByte())
                        writeByte(yOffset.toByte())
                        val mapData = maps["m${xOffset}_$yOffset"]!!
                        writeShort(0)
                        writeShort(mapData.size.toShort())
                        writeBytes(mapData)
                    }
                    // data_loc
                    val data_loc_buffer = buildPacket {
                        writeByte(xOffset.toByte())
                        writeByte(yOffset.toByte())
                        val locData = maps["l${xOffset}_$yOffset"]!!
                        writeShort(0)
                        writeShort(locData.size.toShort())
                        writeBytes(locData)
                    }

                    val data_land_done_buffer = buildPacket {
                        writeByte(xOffset.toByte())
                        writeByte(yOffset.toByte())
                    }

                    val data_loc_done_buffer = buildPacket {
                        writeByte(xOffset.toByte())
                        writeByte(yOffset.toByte())
                    }

                    val player_info_buffer = buildPacket {
                        withBitAccess {
                            writeBit(true)
                            writeBits(2, 3)
                            writeBits(2, 0)
                            writeBits(7, x - 8 * (zoneX - 6))
                            writeBits(7, z - 8 * (zoneZ - 6))
                            writeBits(1, 1)
                            writeBits(1, 0)
                            writeBits(8, 0)
                        }
                    }

                    writeChannel.apply {
                        writeByte((0xff and 132 + serverCipher.getNext()).toByte())
                        writeShort(data_land_buffer.remaining.toShort())
                        writePacket(data_land_buffer)

                        writeByte((0xff and 80 + serverCipher.getNext()).toByte())
                        writePacket(data_land_done_buffer)

                        writeByte((0xff and 220 + serverCipher.getNext()).toByte())
                        writeShort(data_loc_buffer.remaining.toShort())
                        writePacket(data_loc_buffer)

                        writeByte((0xff and 20 + serverCipher.getNext()).toByte())
                        writePacket(data_loc_done_buffer)

                        writeByte((0xff and 184 + serverCipher.getNext()).toByte())
                        writeShort(player_info_buffer.remaining.toShort())
                        writePacket(player_info_buffer)
                    }.flush()
                }
            }
        } else if (opcode == 108) { // keep alive
            val player_info_buffer = buildPacket {
                withBitAccess {
                    writeBit(true)
                    writeBits(2, 3)
                    writeBits(2, 0)
                    writeBits(7, x - 8 * (zoneX - 6))
                    writeBits(7, z - 8 * (zoneZ - 6))
                    writeBits(1, 1)
                    writeBits(1, 0)
                    writeBits(8, 0)
                }
            }
            writeChannel.apply {
                writeByte((0xff and 184 + serverCipher.getNext()).toByte())
                writeShort(player_info_buffer.remaining.toShort())
                writePacket(player_info_buffer)
            }.flush()
        }
    }
}
