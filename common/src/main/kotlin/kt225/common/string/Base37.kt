package kt225.common.string

/**
 * @author Jordan Abraham
 */
val base37 = charArrayOf(
    '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
    'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
    't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
    '3', '4', '5', '6', '7', '8', '9'
)

inline val String.toBase37: Long get() {
    var value = 0L
    repeat(length.coerceAtMost(12)) {
        value *= 37L
        when (val c = this[it]) {
            in 'A'..'Z' -> value += (1 + c.code - 'A'.code).toLong()
            in 'a'..'z' -> value += (1 + c.code - 'a'.code).toLong()
            in '0'..'9' -> value += (27 + c.code - '0'.code).toLong()
        }
    }
    while (value % 37L == 0L && value != 0L) {
        value /= 37L
    }
    return value
}

inline val Long.fromBase37: String get() {
    if (this <= 0L || this >= 0x5b5b57f8a98a5dd1L || this % 37L == 0L) {
        return "invalid_name"
    }
    val builder = StringBuilder(12)
    var next = this
    while (next != 0L) {
        val oldValue = next
        next /= 37L
        builder.append(base37[(oldValue - next * 37L).toInt()])
    }
    return builder.reverse().toString()
}
