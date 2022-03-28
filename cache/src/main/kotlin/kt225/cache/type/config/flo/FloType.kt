package kt225.cache.type.config.flo

import kt225.cache.type.Type

/**
 * @author Jordan Abraham
 */
data class FloType(
    override val id: Int,
    var rgb: Int = 0,
    var textureId: Int = 0,
    var occlude: Boolean = false,
    var name: String = "null",
    var hue: Int = 0,
    var saturation: Int = 0,
    var lightness: Int = 0,
    var blendHue: Int = 0,
    var hsl16: Int = 0,
    var blendHueMultiplier: Int = 0
) : Type(id) {
    fun setColor() {
        val r = (rgb shr 16 and 0xff).toDouble() / 256.0
        val g = (rgb shr 8 and 0xff).toDouble() / 256.0
        val b = (rgb and 0xff).toDouble() / 256.0
        val min = r.coerceAtMost(g).coerceAtMost(b)
        val max = r.coerceAtLeast(g).coerceAtLeast(b)
        var h = 0.0
        var s = 0.0
        val l = (min + max) / 2.0
        if (min != max) {
            when {
                l < 0.5 -> s = (max - min) / (max + min)
                l >= 0.5 -> s = (max - min) / (2.0 - max - min)
            }
            when {
                r == max -> h = (g - b) / (max - min)
                g == max -> h = 2.0 + (b - r) / (max - min)
                b == max -> h = 4.0 + (r - g) / (max - min)
            }
        }
        h /= 6.0
        hue = (h * 256.0).toInt()
        saturation = (s * 256.0).toInt().let { if (it < 0) 0 else if (it > 255) 255 else it }
        lightness = (l * 256.0).toInt().let { if (it < 0) 0 else if (it > 255) 255 else it }
        hsl16 = if (l > 0.5) ((1.0 - l) * s * 512.0).toInt() else (l * s * 512.0).toInt().let { if (it < 1) 1 else it }
        blendHue = (h * hsl16.toDouble()).toInt()
        val randHue = (hue + (Math.random() * 16.0).toInt() - 8).let { if (it < 0) 0 else if (it > 255) 255 else it }
        val randSaturation = (saturation + (Math.random() * 48.0).toInt() - 24).let { if (it < 0) 0 else if (it > 255) 255 else it }
        val randLightness = (lightness + (Math.random() * 48.0).toInt() - 24).let { if (it < 0) 0 else if (it > 255) 255 else it }
        blendHueMultiplier = setHsl16(randHue, randSaturation, randLightness)
    }

    private fun setHsl16(hue: Int, saturation: Int, lightness: Int): Int {
        var s = saturation
        if (lightness > 179) {
            s /= 2
        }
        if (lightness > 192) {
            s /= 2
        }
        if (lightness > 217) {
            s /= 2
        }
        if (lightness > 243) {
            s /= 2
        }
        return (hue / 4 shl 10) + (s / 32 shl 7) + lightness / 2
    }
}
