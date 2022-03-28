package kt225.cache.type.config.varp

import kt225.cache.type.Type

/**
 * @author Jordan Abraham
 */
data class VarpType(
    override val id: Int,
    var type: Int = 0
) : Type(id)
