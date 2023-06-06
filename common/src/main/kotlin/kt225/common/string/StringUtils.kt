package kt225.common.string

/**
 * @author Jordan Abraham
 */
object StringUtils {
    private val base37 = charArrayOf(
        '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
        't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
        '3', '4', '5', '6', '7', '8', '9'
    )

    fun toBase37(str: String): Long {
        var value = 0L
        var n = 0
        while (n < str.length && n < 12) {
            val c = str[n]
            value *= 37L
            when (c) {
                in 'A'..'Z' -> value += (1 + c.code - 65).toLong()
                in 'a'..'z' -> value += (1 + c.code - 97).toLong()
                in '0'..'9' -> value += (27 + c.code - 48).toLong()
            }
            n++
        }
        while (value % 37L == 0L && value != 0L) {
            value /= 37L
        }
        return value
    }

    fun fromBase37(value: Long): String {
        val builder = CharArray(12)
        if (value <= 0L || value >= 0x5b5b57f8a98a5dd1L || value % 37L == 0L) {
            return "invalid_name"
        }
        var next = value
        var len = 0
        while (next != 0L) {
            val oldValue = next
            next /= 37L
            builder[11 - len++] = base37[(oldValue - next * 37L).toInt()]
        }
        return String(builder, 12 - len, len)
    }
}
