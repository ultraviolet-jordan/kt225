package kt225.common.game.entity.animator.type

import kt225.common.game.entity.animator.AnimatorType

/**
 * @author Jordan Abraham
 */
@JvmInline
value class Teleport(
    val packed: Int
) : AnimatorType {
    constructor(
        rendering: Boolean,
        x: Int,
        z: Int,
        plane: Int
    ) : this (
        (if (rendering) 1 else 0) and 0x1
            or (x and 0xff shl 1)
            or (z and 0xff shl 9)
            or (plane and 0x3 shl 17)
    ) {
        require(this.rendering == (if (rendering) 1 else 0))
        require(this.x == x)
        require(this.z == z)
        require(this.plane == plane)
    }

    inline val rendering: Int
        get() = packed and 0x1

    inline val x: Int
        get() = packed shr 1 and 0xff

    inline val z: Int
        get() = packed shr 9 and 0xff

    inline val plane: Int
        get() = packed shr 17 and 0x3
}
