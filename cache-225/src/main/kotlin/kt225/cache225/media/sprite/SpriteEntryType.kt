package kt225.cache225.media.sprite

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class SpriteEntryType(
    val id: Int,
    val name: String,
    var cellWidth: Int = 0,
    var cellHeight: Int = 0,
    var spriteDeltasX: IntArray? = null,
    var spriteDeltasY: IntArray? = null,
    var spriteWidths: IntArray? = null,
    var spriteHeights: IntArray? = null,
    var spritePixels: Array<IntArray>? = null
) : EntryType
