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

/**
 * @author Jordan Abraham
 */
@Singleton
class SpritesProvider @Inject constructor(
    private val media: Media
) : EntryProvider<SpriteEntryType, Sprites<SpriteEntryType>> {
    
    override fun get(): Sprites<SpriteEntryType> {
        val sprites = Sprites<SpriteEntryType>()
        media.keys
            .filter { it != "index.dat".jagNameHash }
            .associateWith(media::read)
            .forEach { (hash, buffer) -> 
                buffer?.let {
                    sprites[hash] = decode(it, SpriteEntryType(hash))
                }
            }
        return sprites
    }
    
    override fun write(entries: Sprites<SpriteEntryType>) {
        val buffer = ByteBuffer.allocate(100_000)
    }
    
    override fun decode(buffer: ByteBuffer, entry: SpriteEntryType): SpriteEntryType {
        val index = media.read("index.dat") ?: error("index.dat file not found.")
        index.position(buffer.g2)
        entry.cellWidth = index.g2
        entry.cellHeight = index.g2
        val palette = IntArray(index.g1)
        for (p in 0 until palette.size - 1) {
            palette[p + 1] = index.g3.let { if (it == 0) 1 else it }
        }
        
        val spriteDeltasX = mutableListOf<Int>()
        val spriteDeltasY = mutableListOf<Int>()
        val spriteWidths = mutableListOf<Int>()
        val spriteHeights = mutableListOf<Int>()
        val spritePixels = mutableListOf<IntArray>()
        
        while (buffer.hasRemaining()) {
            spriteDeltasX.add(index.g1)
            spriteDeltasY.add(index.g1)
            val width = index.g2
            spriteWidths.add(width)
            val height = index.g2
            spriteHeights.add(height)
            val dimensions = width * height
            val pixels = IntArray(dimensions)
            when (index.g1) {
                0 -> {
                    repeat(dimensions) {
                        pixels[it] = palette[buffer.g1]
                    }
                }
                1 -> {
                    repeat(width) { w ->
                        repeat(height) { h ->
                            pixels[w + h * width] = palette[buffer.g1]
                        }
                    }
                }
            }
            spritePixels.add(pixels)
        }
        
        entry.spriteDeltasX = spriteDeltasX.toIntArray()
        entry.spriteDeltasY = spriteDeltasY.toIntArray()
        entry.spriteWidths = spriteWidths.toIntArray()
        entry.spriteHeights = spriteHeights.toIntArray()
        entry.spritePixels = spritePixels.toTypedArray()
        return entry
    }

    override fun encode(buffer: ByteBuffer, entry: SpriteEntryType) {
    }
}
