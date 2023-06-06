package kt225.common.game.entity.render.type

import kt225.common.game.entity.render.RenderType

/**
 * @author Jordan Abraham
 */
data class Sequence(
    val id: Int,
    val delay: Int
) : RenderType {
    constructor(id: Int) : this(id, 0)
}
