package kt225.common.packet.server

/**
 * @author Jordan Abraham
 */
data class MapRequest(
    val type: Int,
    val x: Int,
    val z: Int,
    val name: String
)
