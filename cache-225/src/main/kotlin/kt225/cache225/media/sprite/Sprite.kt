package kt225.cache225.media.sprite

/**
 * @author Jordan Abraham
 */
data class Sprite(
    val offset: Int,
    val deltaX: Int,
    val deltaY: Int,
    val width: Int,
    val height: Int,
    val format: Int,
    val raster: IntArray
)
