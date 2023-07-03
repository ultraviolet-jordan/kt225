package kt225.common.game.entity.animator.type

import kt225.common.game.entity.animator.AnimatorType

/**
 * @author Jordan Abraham
 */
@JvmInline
value class Run(
    val packed: Int
) : AnimatorType {
    constructor(
        rendering: Boolean,
        walkDirection: Int,
        runDirection: Int
    ) : this(
        (if (rendering) 1 else 0) and 0x1
            or (walkDirection and 0x7f shl 1)
            or (runDirection and 0x7f shl 8)
    ) {
        require(this.rendering == (if (rendering) 1 else 0))
        require(this.walkDirection == walkDirection)
        require(this.runDirection == runDirection)
    }

    inline val rendering: Int
        get() = packed and 0x1

    inline val walkDirection: Int
        get() = packed shr 1 and 0x7f

    inline val runDirection: Int
        get() = packed shr 8 and 0x7f
}
