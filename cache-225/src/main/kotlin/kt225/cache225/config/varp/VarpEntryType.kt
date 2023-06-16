package kt225.cache225.config.varp

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class VarpEntryType(
    val id: Int,
    var opcode1: Int = 0,
    var opcode2: Int = 0,
    var opcode3: Boolean = false,
    var opcode4: Boolean = true,
    var clientCode: Int = 0,
    var opcode6: Boolean = false,
    var opcode7: Int = 0,
    var opcode8: Boolean = false,
    var opcode10: String? = null
) : EntryType
