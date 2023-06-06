package kt225.common.game.entity.render

/**
 * @author Jordan Abraham
 */
abstract class RenderBlock<R : RenderType>(
    val renderType: R,
    val builder: RenderBlockBuilder<R>
)
