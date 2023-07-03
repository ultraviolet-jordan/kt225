package kt225.game.world.map

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import org.rsmod.pathfinder.StepValidator
import org.rsmod.pathfinder.ZoneFlags

/**
 * @author Jordan Abraham
 */
@Singleton
class StepValidatorProvider @Inject constructor(
    private val zoneFlags: ZoneFlags
) : Provider<StepValidator> {
    override fun get(): StepValidator {
        return StepValidator(zoneFlags.flags)
    }
}
