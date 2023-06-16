package kt225.common.network

/**
 * @author Jordan Abraham
 */
data class LoginRequest(
    val properties: Int,
    val uid: Int,
    val username: String,
    val password: String,
    val clientSeed: IntArray,
    val serverSeed: IntArray,
    val session: Session
)
