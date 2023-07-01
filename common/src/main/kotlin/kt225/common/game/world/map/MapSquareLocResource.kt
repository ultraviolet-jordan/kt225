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
        (coords and COORDINATES_MASK).toLong() 
            or ((loc and LOC_MASK).toLong() shl LOC_BITS)
    ) {
        require(this.coords == coords)
        require(this.loc == loc)
    }

    /**
     * The MapSquareCoordinates of this resource.
     */
    inline val coords: Int 
        get() = (packed and COORDINATES_MASK_LONG).toInt()

    /**
     * The MapSquareLoc of this resource.
     */
    inline val loc: Int 
        get() = (packed shr LOC_BITS and LOC_MASK_LONG).toInt()
    
    companion object {
        const val COORDINATES_MASK = 0x3ffff
        const val COORDINATES_MASK_LONG = COORDINATES_MASK.toLong()
        const val LOC_BITS = 18
        const val LOC_MASK = 0xffffff
        const val LOC_MASK_LONG = LOC_MASK.toLong()
    }
}
