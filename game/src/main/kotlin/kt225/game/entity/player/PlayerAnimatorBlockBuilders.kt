package kt225.game.entity.player

import kt225.common.game.entity.animator.AnimatorBlockBuilder
import kt225.common.game.entity.animator.type.Render
import kt225.common.game.entity.animator.type.Run
import kt225.common.game.entity.animator.type.Teleport
import kt225.common.game.entity.animator.type.Walk
import kt225.packet.builder.animator.PlayerRenderAnimatorBlockBuilder
import kt225.packet.builder.animator.PlayerRunAnimatorBlockBuilder
import kt225.packet.builder.animator.PlayerTeleportAnimatorBlockBuilder
import kt225.packet.builder.animator.PlayerWalkAnimatorBlockBuilder
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object PlayerAnimatorBlockBuilders : Map<KClass<*>, AnimatorBlockBuilder<*>> by mapOf(
    Render::class to PlayerRenderAnimatorBlockBuilder(),
    Walk::class to PlayerWalkAnimatorBlockBuilder(),
    Run::class to PlayerRunAnimatorBlockBuilder(),
    Teleport::class to PlayerTeleportAnimatorBlockBuilder()
)
