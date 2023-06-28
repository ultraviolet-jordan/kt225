package kt225.common.string // ktlint-disable filename

/**
 * @author Jordan Abraham
 */
val String.jagNameHash: Int get() = uppercase().fold(0) { hash, char -> hash * 61 + char.code - 32 }
