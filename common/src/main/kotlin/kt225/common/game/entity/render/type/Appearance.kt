package kt225.common.game.entity.render.type

import kt225.common.game.entity.render.RenderType
import kt225.common.game.entity.render.type.kit.BodyPart
import kt225.common.game.entity.render.type.kit.BodyPartColor
import kt225.common.game.entity.render.type.kit.Gender

/**
 * @author Jordan Abraham
 */
data class Appearance(
    val gender: Gender = Gender.MALE,
    val headIcon: Int,
    val name: String,
    val combatLevel: Int,
    val bodyParts: IntArray = IntArray(7),
    val bodyPartColors: IntArray = IntArray(5),
    val renderSequences: IntArray = intArrayOf(808, 823, 819, 820, 821, 822, 824)
) : RenderType {
    init {
        bodyParts[BodyPart.HEAD] = 0
        bodyParts[BodyPart.JAW] = 10
        bodyParts[BodyPart.TORSO] = 18
        bodyParts[BodyPart.ARMS] = 26
        bodyParts[BodyPart.HANDS] = 33
        bodyParts[BodyPart.LEGS] = 36
        bodyParts[BodyPart.FEET] = 42
        bodyPartColors[BodyPartColor.HAIR] = 0
        bodyPartColors[BodyPartColor.TORSO] = 0
        bodyPartColors[BodyPartColor.LEGS] = 0
        bodyPartColors[BodyPartColor.FEET] = 0
        bodyPartColors[BodyPartColor.SKIN] = 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Appearance

        if (gender != other.gender) return false
        if (headIcon != other.headIcon) return false
        if (name != other.name) return false
        if (combatLevel != other.combatLevel) return false
        if (!bodyParts.contentEquals(other.bodyParts)) return false
        if (!bodyPartColors.contentEquals(other.bodyPartColors)) return false
        return renderSequences.contentEquals(other.renderSequences)
    }

    override fun hashCode(): Int {
        var result = gender.hashCode()
        result = 31 * result + headIcon
        result = 31 * result + name.hashCode()
        result = 31 * result + combatLevel
        result = 31 * result + bodyParts.contentHashCode()
        result = 31 * result + bodyPartColors.contentHashCode()
        result = 31 * result + renderSequences.contentHashCode()
        return result
    }
}
