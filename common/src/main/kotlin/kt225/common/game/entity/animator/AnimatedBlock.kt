package kt225.common.game.entity.animator

/**
 * @author Jordan Abraham
 */
class AnimatedBlock<A : AnimatorType>(
    animatorType: A,
    builder: AnimatorBlockBuilder<A>
) : AnimatorBlock<A>(animatorType, builder)
