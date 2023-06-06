package kt225.common.game.entity.animator

/**
 * @author Jordan Abraham
 */
abstract class AnimatorBlock<A : AnimatorType>(
    val animatorType: A,
    val builder: AnimatorBlockBuilder<A>
)
