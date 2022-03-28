package kt225.cache

import kt225.cache.archive.Archive
import kt225.cache.archive.type.obj.ObjTypeLoader
import kt225.cache.archive.type.seq.SeqTypeLoader
import org.koin.dsl.module

/**
 * @author Jordan Abraham
 */
val cacheModule = module(createdAtStart = true) {
    single { SeqTypeLoader() }
    single { ObjTypeLoader() }
}

fun cacheResource(name: String) = Archive::class.java.getResourceAsStream("/archives/$name")!!
