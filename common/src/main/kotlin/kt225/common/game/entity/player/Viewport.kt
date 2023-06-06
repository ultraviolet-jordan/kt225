package kt225.common.game.entity.player

/**
 * @author Jordan Abraham
 */
class Viewport {
    val players = arrayOfNulls<Player?>(2048)
    val localRenderUpdates = ArrayList<Int>()

    var forceViewDistance = false
        private set
    var viewDistance = PREFERRED_VIEW_DISTANCE
        private set

    private var resizeTickCount = 0

    fun resize() {
        // Thank you Kris =)
        if (forceViewDistance) return
        if (players.count { it != null } >= PREFERRED_PLAYER_COUNT) {
            if (viewDistance > 0) viewDistance--
            resizeTickCount = 0
            return
        }
        if (++resizeTickCount >= RESIZE_CHECK_INTERVAL) {
            if (viewDistance < PREFERRED_VIEW_DISTANCE) {
                viewDistance++
            } else {
                resizeTickCount = 0
            }
        }
    }

    private companion object {
        const val RESIZE_CHECK_INTERVAL = 10
        const val PREFERRED_PLAYER_COUNT = 250
        const val PREFERRED_VIEW_DISTANCE = 15
    }
}
