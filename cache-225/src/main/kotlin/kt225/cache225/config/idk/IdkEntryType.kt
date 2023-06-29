@file:Suppress("DuplicatedCode", "PropertyName")

package kt225.cache225.config.idk

import kt225.cache.EntryType

/**
 * @author Jordan Abraham
 */
data class IdkEntryType(
    val id: Int,
    var type: Int = -1,
    var models: IntArray? = null,
    var disable: Boolean = false,
    var recol_s: IntArray = IntArray(6),
    var recol_d: IntArray = IntArray(6),
    var headModels: IntArray = IntArray(5) { -1 }
) : EntryType {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IdkEntryType

        if (id != other.id) return false
        if (type != other.type) return false
        if (models != null) {
            if (other.models == null) return false
            if (!models.contentEquals(other.models)) return false
        } else if (other.models != null) return false
        if (disable != other.disable) return false
        if (!recol_s.contentEquals(other.recol_s)) return false
        if (!recol_d.contentEquals(other.recol_d)) return false
        return headModels.contentEquals(other.headModels)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + type
        result = 31 * result + (models?.contentHashCode() ?: 0)
        result = 31 * result + disable.hashCode()
        result = 31 * result + recol_s.contentHashCode()
        result = 31 * result + recol_d.contentHashCode()
        result = 31 * result + headModels.contentHashCode()
        return result
    }
}
