package kt225.cache225.config.flo

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class FloEntryType(
    val id: Int,
    var rgb: Int? = null,
    var texture: Int = -1,
    var opcode3: Boolean = false,
    var occlude: Boolean = true,
    var name: String? = null
) : EntryType
