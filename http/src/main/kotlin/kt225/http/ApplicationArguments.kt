package kt225.http

/**
 * @author Jordan Abraham
 */
data class ApplicationArguments(
    val args: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationArguments

        return args.contentEquals(other.args)
    }

    override fun hashCode(): Int = args.contentHashCode()
}
