package kt225.common.game.entity.animator

import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
abstract class AnimatorBlockBuilder<out A : AnimatorType>(
    val index: Int
) {
    abstract fun buildAnimatorBlock(buffer: ByteBuffer, animator: @UnsafeVariance A)
}
