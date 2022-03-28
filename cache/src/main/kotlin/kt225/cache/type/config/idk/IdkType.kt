package kt225.cache.type.config.idk

import kt225.cache.type.Type

/**
 * @author Jordan Abraham
 */
data class IdkType(
    override val id: Int,
    var type: Int = -1,
    var validStyle: Boolean = false,
    var oldColors: List<Int> = List(6) { 0 },
    var newColors: List<Int> = List(6) { 0 },
    var headModelIds: List<Int> = List(5) { -1 },
    var modelIds: List<Int> = emptyList()
) : Type(id)
