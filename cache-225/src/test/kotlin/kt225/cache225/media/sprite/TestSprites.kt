package kt225.cache225.media.sprite

import com.google.inject.Guice
import dev.misfitlabs.kotlinguice4.getInstance
import kt225.cache.CacheModule
import kt225.cache.media.sprite.Sprites
import kt225.cache225.Cache225Module
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.math.max
import kotlin.math.min
import kotlin.test.Test

/**
 * @author Jordan Abraham
 */
class TestSprites {
    @Test
    fun `test all`() {
        val injector = Guice.createInjector(CacheModule, Cache225Module)
        val sprites = injector.getInstance<Sprites<SpriteEntryType>>()

        val path = Path("./sprites/")
        if (path.notExists()) path.createDirectories()

        sprites.values.forEach {
            val length = it.spritePixels?.size ?: 0
            val cellWidth = it.cellWidth
            val cellHeight = it.cellHeight
            val rows = min(length, max(1, length * cellWidth / 400))
            val columns = max(1, length / rows)

            val spriteSheet = BufferedImage(cellWidth * columns, cellHeight * rows, BufferedImage.TYPE_INT_ARGB).apply { 
                setRGB(0, 0, width, height, IntArray(width * height) { Color.WHITE.rgb }, 0, width)
            }
            
            repeat(rows) { row ->
                repeat(columns) { column ->
                    val index = row * columns + column
                    val offsetX = it.spriteDeltasX?.get(index) ?: error("Missing sprite deltaX at index $index.")
                    val offsetY = it.spriteDeltasY?.get(index) ?: error("Missing sprite deltaY at index $index.")
                    val width = it.spriteWidths?.get(index) ?: error("Missing sprite width at index $index.")
                    val height = it.spriteHeights?.get(index) ?: error("Missing sprite height at index $index.")
                    val pixels = it.spritePixels?.get(index)?.map { rgb -> if (rgb == 0) Color(0xFF00FF).rgb else Color(rgb).rgb }?.toIntArray()
                    spriteSheet.setRGB(column * cellWidth + offsetX, row * cellHeight + offsetY, width, height, pixels, 0, width)
                }
            }

            ImageIO.write(spriteSheet, "png", File(path.toString(), "${it.id}.png"))
        }
    }
}
