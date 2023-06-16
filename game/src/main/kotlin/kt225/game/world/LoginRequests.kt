package kt225.game.world

import kt225.common.game.entity.Entity
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
class LoginRequests : MutableSet<Entity> by ConcurrentHashMap.newKeySet()
