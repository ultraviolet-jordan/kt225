package kt225.network.codec.encode

import com.google.inject.Inject
import com.google.inject.Singleton
import io.ktor.utils.io.ByteWriteChannel
import kt225.common.game.world.World
import kt225.common.network.CodecEncoder
import kt225.common.network.LoginRequest
import kt225.common.network.Session
import kt225.network.codec.decode.GamePacketDecoder
import kt225.network.codec.type.BadSessionLoginResponse
import kt225.network.codec.type.CouldNotCompleteLoginResponse
import kt225.network.codec.type.LoginResponse
import kt225.network.codec.type.RuneScapeHasBeenUpdatedLoginResponse
import kt225.network.codec.type.SuccessfulLoginResponse
import kt225.network.codec.type.SuccessfulReconnectLoginResponse

/**
 * @author Jordan Abraham
 */
@Singleton
class LoginEncoder @Inject constructor(
    private val world: World
) : CodecEncoder<LoginResponse> {
    override suspend fun encode(session: Session, message: LoginResponse, buffer: ByteWriteChannel) {
        when {
            message.loginType != 16 && message.loginType != 18 -> session.codec(
                type = CouldNotCompleteLoginEncoder::class,
                message = CouldNotCompleteLoginResponse()
            )
            message.version != 225 -> session.codec(
                type = RuneScapeHasBeenUpdatedLoginEncoder::class,
                message = RuneScapeHasBeenUpdatedLoginResponse()
            )
            !message.crcs.contentEquals(session.crcs) -> session.codec(
                type = RuneScapeHasBeenUpdatedLoginEncoder::class,
                message = RuneScapeHasBeenUpdatedLoginResponse()
            )
            message.rsaTen != 10 -> session.codec(
                type = BadSessionLoginEncoder::class,
                message = BadSessionLoginResponse()
            )
            else -> {
                val loginRequest = LoginRequest(
                    properties = message.properties,
                    uid = message.uid,
                    username = message.username,
                    password = message.password,
                    clientSeed = message.clientSeed,
                    serverSeed = message.serverSeed,
                    session = session
                )
                world.requestLogin(loginRequest)

                if (message.loginType == 16) {
                    session.codec(
                        type = SuccessfulLoginEncoder::class,
                        message = SuccessfulLoginResponse()
                    )
                } else {
                    session.codec(
                        type = SuccessfulReconnectLoginEncoder::class,
                        message = SuccessfulReconnectLoginResponse()
                    )
                }
                session.codec(GamePacketDecoder::class)
            }
        }
    }
}
