package kt225.game.entity.player

import kt225.common.game.entity.render.HighDefinitionRenderBlock
import kt225.common.game.entity.render.RenderType
import kt225.common.game.entity.render.Renderer

/**
 * @author Jordan Abraham
 */
class PlayerRenderer : Renderer(
    lowDefinitionRenderBlocks = arrayOfNulls(9),
    highDefinitionRenderBlocks = arrayOfNulls(9)
) {
    override fun <R : RenderType> render(type: R): R? {
        val builder = PlayerRendererBlockBuilders[type::class] ?: return null
        highDefinitionRenderBlocks[builder.index] = HighDefinitionRenderBlock(type, builder)
        return type
    }
}
