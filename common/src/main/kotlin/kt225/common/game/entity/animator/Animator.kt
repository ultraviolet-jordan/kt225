package kt225.common.game.entity.animator

/**
 * @author Jordan Abraham
 */
interface Animator {
    fun <A : AnimatorType> animate(type: A): A?
    fun block(): AnimatedBlock<*>?
    fun clear()
}
