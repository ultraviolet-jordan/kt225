package kt225.cache.archive.inter

import com.google.inject.Provider
import com.google.inject.Singleton

/**
 * @author Jordan Abraham
 */
@Singleton
class InterfaceArchiveProvider : Provider<InterfaceArchive> {
    override fun get(): InterfaceArchive = InterfaceArchive()
}
