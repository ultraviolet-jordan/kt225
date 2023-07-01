package kt225.common.game.world.map

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MapSquareLocResource(
    val packed: Long
) {
    constructor(
        coords: Int,
        loc: Int
    ) : this(
        (coords.toLong() and COORDINATES_MASK)
            or (loc.toLong() and LOC_MASK shl LOC_BITS)
    ) {
        require(this.coords == coords)
        require(this.loc == loc)
    }

    /**
     * The MapSquareCoordinates of this resource.
     */
    inline val coords: Int 
        get() = (packed and COORDINATES_MASK).toInt()

    /**
     * The MapSquareLoc of this resource.
     */
    inline val loc: Int 
        get() = (packed shr LOC_BITS and LOC_MASK).toInt()
    
    companion object {
        const val COORDINATES_MASK = 0x3ffffL
        const val LOC_BITS = 18
        const val LOC_MASK = 0xffffffL
    }
}
