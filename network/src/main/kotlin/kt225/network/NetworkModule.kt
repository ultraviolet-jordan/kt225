package kt225.network

import dev.misfitlabs.kotlinguice4.KotlinModule
import dev.misfitlabs.kotlinguice4.multibindings.KotlinMultibinder
import kt225.common.network.CodecDecoder
import kt225.common.network.CodecEncoder
import kt225.common.network.CodecEncoderType
import kt225.network.codec.decode.GamePacketDecoder
import kt225.network.codec.decode.LoginDecoder
import kt225.network.codec.encode.BadSessionLoginEncoder
import kt225.network.codec.encode.CouldNotCompleteLoginEncoder
import kt225.network.codec.encode.InvalidUsernameOrPasswordLoginEncoder
import kt225.network.codec.encode.LoginAttemptsExceededLoginEncoder
import kt225.network.codec.encode.LoginEncoder
import kt225.network.codec.encode.LoginLimitExceededLoginEncoder
import kt225.network.codec.encode.LoginServerOfflineLoginEncoder
import kt225.network.codec.encode.LoginServerRejectedSessionLoginEncoder
import kt225.network.codec.encode.RuneScapeHasBeenUpdatedLoginEncoder
import kt225.network.codec.encode.ServerSeedEncoder
import kt225.network.codec.encode.SuccessfulLoginEncoder
import kt225.network.codec.encode.SuccessfulModeratorLoginEncoder
import kt225.network.codec.encode.SuccessfulReconnectLoginEncoder
import kt225.network.codec.encode.ThisServerIsBeingUpdatedLoginEncoder
import kt225.network.codec.encode.ThisWorldIsFullLoginEncoder
import kt225.network.codec.encode.YouAreStandingInAMembersOnlyAreaLoginEncoder
import kt225.network.codec.encode.YouNeedAMembersAccountToLoginThisWorldLoginEncoder
import kt225.network.codec.encode.YourAccountHasBeenDisabledLoginEncoder
import kt225.network.codec.encode.YourAccountIsAlreadyLoggedInLoginEncoder

/**
 * @author Jordan Abraham
 */
object NetworkModule : KotlinModule() {
    override fun configure() {
        KotlinMultibinder.newSetBinder<CodecEncoder<CodecEncoderType>>(kotlinBinder).apply {
            addBinding().to<BadSessionLoginEncoder>()
            addBinding().to<CouldNotCompleteLoginEncoder>()
            addBinding().to<InvalidUsernameOrPasswordLoginEncoder>()
            addBinding().to<LoginAttemptsExceededLoginEncoder>()
            addBinding().to<LoginEncoder>()
            addBinding().to<LoginLimitExceededLoginEncoder>()
            addBinding().to<LoginServerOfflineLoginEncoder>()
            addBinding().to<LoginServerRejectedSessionLoginEncoder>()
            addBinding().to<RuneScapeHasBeenUpdatedLoginEncoder>()
            addBinding().to<ServerSeedEncoder>()
            addBinding().to<SuccessfulLoginEncoder>()
            addBinding().to<SuccessfulModeratorLoginEncoder>()
            addBinding().to<SuccessfulReconnectLoginEncoder>()
            addBinding().to<ThisServerIsBeingUpdatedLoginEncoder>()
            addBinding().to<ThisWorldIsFullLoginEncoder>()
            addBinding().to<YouAreStandingInAMembersOnlyAreaLoginEncoder>()
            addBinding().to<YouNeedAMembersAccountToLoginThisWorldLoginEncoder>()
            addBinding().to<YourAccountHasBeenDisabledLoginEncoder>()
            addBinding().to<YourAccountIsAlreadyLoggedInLoginEncoder>()
        }

        KotlinMultibinder.newSetBinder<CodecDecoder>(kotlinBinder).apply {
            addBinding().to<LoginDecoder>()
            addBinding().to<GamePacketDecoder>()
        }
    }
}
