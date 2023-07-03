package kt225.common.game.entity.animator.type

import kt225.common.game.entity.animator.AnimatorType

/**
 * @author Jordan Abraham
 */
@JvmInline
value class Walk(
    val packed: Int
) : AnimatorType {
    constructor(
        rendering: Boolean,
        direction: Int
    ) : this(
        (if (rendering) 1 else 0) and 0x1
            or (direction and 0x7f shl 1)
    ) {
        require(this.rendering == (if (rendering) 1 else 0))
        require(this.direction == direction)
    }
    
    inline val rendering: Int
        get() = packed and 0x1
    
    inline val direction: Int
        get() = packed shr 1 and 0x7f
}
