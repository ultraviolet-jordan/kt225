package kt225.common.game.entity.route

import kt225.common.game.entity.EntityDirection

/**
 * @author Jordan Abraham
 */
@JvmInline
value class RouteStepDirection(
    val packed: Int
) {
    constructor(
        walkDirection: EntityDirection,
        runDirection: EntityDirection
    ) : this (
        walkDirection.id and 0x7f
            or (runDirection.id and 0x7f shl 7)
    ) {
        require(this.walkDirection == walkDirection.id)
        require(this.runDirection == runDirection.id)
    }
    
    inline val walkDirection: Int
        get() = packed and 0x7f

    inline val runDirection: Int
        get() = packed shr 7 and 0x7f
}
