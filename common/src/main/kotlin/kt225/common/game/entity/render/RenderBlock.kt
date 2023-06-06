package kt225.common.game.entity.render

/**
 * @author Jordan Abraham
 */
abstract class RenderBlock<R : RenderType> {
    abstract fun renderType(): R
    abstract fun builder(): RenderBlockBuilder<R>
}
