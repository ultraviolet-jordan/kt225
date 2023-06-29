@file:Suppress("DuplicatedCode")

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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sprite

        if (offset != other.offset) return false
        if (deltaX != other.deltaX) return false
        if (deltaY != other.deltaY) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (format != other.format) return false
        return raster.contentEquals(other.raster)
    }

    override fun hashCode(): Int {
        var result = offset
        result = 31 * result + deltaX
        result = 31 * result + deltaY
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + format
        result = 31 * result + raster.contentHashCode()
        return result
    }
}
