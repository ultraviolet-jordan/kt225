package kt225.common.game.entity.animator

import kt225.common.buffer.BitAccess

/**
 * @author Jordan Abraham
 */
abstract class AnimatorBlockBuilder<out A : AnimatorType>(
    val index: Int
) {
    abstract fun buildAnimatorBlock(buffer: BitAccess, animator: @UnsafeVariance A)
}
