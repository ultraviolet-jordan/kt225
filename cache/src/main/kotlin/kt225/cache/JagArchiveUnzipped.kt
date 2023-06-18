package kt225.cache

import java.util.TreeMap

/**
 * @author Jordan Abraham
 */
data class JagArchiveUnzipped(
    val bytes: ByteArray,
    val isCompressed: Boolean,
    val crc: Int,
    val files: MutableMap<Int, JagArchiveFile> = TreeMap()
)
