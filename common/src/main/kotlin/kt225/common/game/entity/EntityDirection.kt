package kt225.common.game.entity

import kt225.common.game.world.Coordinates

/**
 * @author Jordan Abraham
 */
@JvmInline
value class EntityDirection(
    val id: Int
) {
    inline val deltaX: Int
        get() = when (this) {
            SOUTH_EAST, NORTH_EAST, EAST -> 1
            SOUTH_WEST, NORTH_WEST, WEST -> -1
            else -> 0
        }

    inline val deltaZ: Int
        get() = when (this) {
            NORTH_WEST, NORTH_EAST, NORTH -> 1
            SOUTH_WEST, SOUTH_EAST, SOUTH -> -1
            else -> 0
        }
    
    companion object {
        val NORTH_WEST = EntityDirection(0)
        val NORTH = EntityDirection(1)
        val NORTH_EAST = EntityDirection(2)
        val WEST = EntityDirection(3)
        val EAST = EntityDirection(4)
        val SOUTH_WEST = EntityDirection(5)
        val SOUTH = EntityDirection(6)
        val SOUTH_EAST = EntityDirection(7)
        val NONE = EntityDirection(127)

        fun between(start: Coordinates, end: Coordinates): EntityDirection {
            val deltaX = end.x - start.x
            return when (end.z - start.z) {
                1 -> when (deltaX) {
                    1 -> NORTH_EAST
                    0 -> NORTH
                    -1 -> NORTH_WEST
                    else -> NONE
                }
                -1 -> when (deltaX) {
                    1 -> SOUTH_EAST
                    0 -> SOUTH
                    -1 -> SOUTH_WEST
                    else -> NONE
                }
                0 -> when (deltaX) {
                    1 -> EAST
                    0 -> NONE
                    -1 -> WEST
                    else -> NONE
                }
                else -> NONE
            }
        }
    }
}
