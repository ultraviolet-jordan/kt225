package kt225.common.crypto

/**
 * @author Jordan Abraham
 */
class IsaacRandom(
    private val rsl: IntArray,
    private val mem: IntArray
) {
    private var randa = 0
    private var randb = 0
    private var randc = 0
    private var count = 0

    val nextInt: Int get() {
        if (count-- == 0) {
            isaac()
            count = 255
        }
        return rsl[count]
    }
    
    init {
        val mixer = IntArray(8) { 0x9E3779B9.toInt() }
        repeat(4) {
            mixer.mix(it, firstPass = true)
        }
        for (index in 0 until 256 step 8) {
            repeat(mixer.size) {
                mixer[it] += rsl[index + it]
            }
            mixer.mix(index)
        }
        for (index in 0 until 256 step 8) {
            repeat(mixer.size) {
                mixer[it] += mem[index + it]
            }
            mixer.mix(index)
        }
        isaac()
        count = 256
    }

    private fun IntArray.mix(position: Int, firstPass: Boolean = false) {
        var a = this[0]
        var b = this[1]
        var c = this[2]
        var d = this[3]
        var e = this[4]
        var f = this[5]
        var g = this[6]
        var h = this[7]
        a = a xor (b shl 11); /****/d += a; b += c
        b = b xor (c ushr 2); /****/e += b; c += d
        c = c xor (d shl 8); /*****/f += c; d += e
        d = d xor (e ushr 16); /***/g += d; e += f
        e = e xor (f shl 10); /****/h += e; f += g
        f = f xor (g ushr 4); /****/a += f; g += h
        g = g xor (h shl 8); /*****/b += g; h += a
        h = h xor (a ushr 9); /****/c += h; a += b
        this[0] = a
        this[1] = b
        this[2] = c
        this[3] = d
        this[4] = e
        this[5] = f
        this[6] = g
        this[7] = h
        if (!firstPass) {
            p8(position)
        }
    }

    private fun IntArray.p8(position: Int) {
        repeat(size) {
            mem[position + it] = this[it]
        }
    }

    private fun isaac() {
        randb += ++randc
        repeat(256) { index ->
            when (index and 0x3) {
                0 -> randa = randa xor (randa shl 13)
                1 -> randa = randa xor (randa ushr 6)
                2 -> randa = randa xor (randa shl 2)
                3 -> randa = randa xor (randa ushr 16)
            }
            val position = mem[index]
            randa += mem[(index + 128) and 0xff]
            (mem[position shr 2 and 0xFF] + randa + randb)
                .also { mem[index] = it }
                .also { randb = (mem[it shr 8 shr 2 and 0xFF] + position).also { next -> rsl[index] = next } }
        }
    }

    companion object {
        fun create(src: IntArray): IsaacRandom {
            return IsaacRandom(src.copyInto(IntArray(256)), IntArray(256))
        }
    }
}
