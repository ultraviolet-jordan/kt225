package kt225.cache225.media.sprite

import com.google.inject.Inject
import com.google.inject.Singleton
import kt225.cache.EntryProvider
import kt225.cache.media.Media
import kt225.cache.media.sprite.Sprites
import kt225.common.buffer.g1
import kt225.common.buffer.g2
import kt225.common.buffer.g3
import kt225.common.string.jagNameHash
import java.nio.ByteBuffer
import java.util.Arrays

/**
 * @author Jordan Abraham
 */
@Singleton
class SpritesProvider @Inject constructor(
    private val media: Media,
    private val glossary: SpriteGlossary
) : EntryProvider<SpriteEntryType, Sprites<SpriteEntryType>> {
    
    override fun get(): Sprites<SpriteEntryType> {
        val sprites = Sprites<SpriteEntryType>()
        media.keys
            .filter { it != "index.dat".jagNameHash }
            .associateWith(media::read)
            .forEach { (hash, buffer) -> 
                buffer?.let {
                    val name = glossary[hash] ?: hash.toString()
                    sprites[hash] = decode(it, SpriteEntryType(hash, name))
                }
            }
        return sprites
    }
    
    override fun write(entries: Sprites<SpriteEntryType>) {
    }
    
    override fun decode(buffer: ByteBuffer, entry: SpriteEntryType): SpriteEntryType {
        val index = media.read("index.dat") ?: error("index.dat file not found.")
        index.position(buffer.g2)
        entry.cellWidth = index.g2
        entry.cellHeight = index.g2
        val palette = IntArray(index.g1)
        for (i in 1 until palette.size) {
            palette[i] = index.g3.let { if (it == 0) 1 else it }
        }
        val sprites = mutableListOf<Sprite>().apply { 
            decodeSprite(index, buffer, palette, 0)
        }
        entry.sprites = sprites
        return entry
    }

    override fun encode(buffer: ByteBuffer, entry: SpriteEntryType) {
    }
    
    private tailrec fun MutableList<Sprite>.decodeSprite(index: ByteBuffer, buffer: ByteBuffer, palette: IntArray, offset: Int) {
        if (!buffer.hasRemaining()) {
            return
        }
        val deltaX = index.g1
        val deltaY = index.g1
        val width = index.g2
        val height = index.g2
        val format = index.g1
        val raster = IntArray(width * height)
        when (format) {
            // Column.
            0 -> Arrays.setAll(raster) { palette.color(buffer.g1) }
            // Row.
            1 -> {
                repeat(width) { w ->
                    repeat(height) { h ->
                        raster[w + h * width] = palette.color(buffer.g1)
                    }
                }
            }
        }
        add(Sprite(offset, deltaX, deltaY, width, height, format, raster))
        return decodeSprite(index, buffer, palette, offset + 1)
    }
    
    private fun IntArray.color(index: Int): Int {
        return this[index].let { if (it == 0) 0xff00ff or 0xff000000.toInt() else it or 0xff000000.toInt() }
    }
}
