package kt225.game.entity.player

import kt225.common.game.entity.render.RenderBlockBuilder
import kt225.common.game.entity.render.type.Appearance
import kt225.common.game.entity.render.type.Sequence
import kt225.packet.builder.render.PlayerAppearanceRenderBlockBuilder
import kt225.packet.builder.render.PlayerSequenceRenderBlockBuilder
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object PlayerRendererBlockBuilders : Map<KClass<*>, RenderBlockBuilder<*>> by mapOf(
    Appearance::class to PlayerAppearanceRenderBlockBuilder(),
    Sequence::class to PlayerSequenceRenderBlockBuilder()
)
