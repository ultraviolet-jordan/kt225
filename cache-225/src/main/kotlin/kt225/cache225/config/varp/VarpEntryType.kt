package kt225.cache225.config.varp

import kt225.cache.archive.EntryType

/**
 * @author Jordan Abraham
 */
data class VarpEntryType(
    val id: Int,
    var type: Int = 0
) : EntryType
