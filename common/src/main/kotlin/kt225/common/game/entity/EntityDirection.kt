package kt225.common.game.entity

import kt225.common.game.world.Coordinates

/**
 * @author Jordan Abraham
 */
enum class EntityDirection(
    val id: Int
) {
    NONE(-1),
    NORTH_WEST(0),
    NORTH(1),
    NORTH_EAST(2),
    WEST(3),
    EAST(4),
    SOUTH_WEST(5),
    SOUTH(6),
    SOUTH_EAST(7);

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
        fun between(start: Coordinates, end: Coordinates): EntityDirection {
            val deltaX = end.x - start.x
            val deltaZ = end.z - start.z
            when (deltaZ) {
                1 -> {
                    when (deltaX) {
                        1 -> return NORTH_EAST
                        0 -> return NORTH
                        -1 -> return NORTH_WEST
                    }
                }
                -1 -> {
                    when (deltaX) {
                        1 -> return SOUTH_EAST
                        0 -> return SOUTH
                        -1 -> return SOUTH_WEST
                    }
                }
                0 -> {
                    when (deltaX) {
                        1 -> return EAST
                        0 -> return NONE
                        -1 -> return WEST
                    }
                }
            }
            throw IllegalArgumentException("Invalid deltas $deltaX, $deltaZ.")
        }
    }
}
