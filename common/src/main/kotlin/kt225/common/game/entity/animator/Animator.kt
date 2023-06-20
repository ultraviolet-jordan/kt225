package kt225.common.game.entity.animator

/**
 * @author Jordan Abraham
 */
abstract class Animator {
    var block: AnimatedBlock<*>? = null
    
    abstract fun <A : AnimatorType> animate(type: A): A?
    
    fun clear() {
        block = null
    }
}
