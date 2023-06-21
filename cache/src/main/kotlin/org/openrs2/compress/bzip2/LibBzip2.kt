package org.openrs2.compress.bzip2

import jnr.ffi.LibraryLoader
import jnr.ffi.LibraryOption
import jnr.ffi.Runtime
import jnr.ffi.Struct
import jnr.ffi.annotations.Direct

@Suppress("unused")
interface LibBzip2 {
    class BzStream(runtime: Runtime) : Struct(runtime) {
        val nextIn: Pointer = Pointer()
        val availIn: Unsigned32 = Unsigned32()
        val totalInLo32: Unsigned32 = Unsigned32()
        val totalInHi32: Unsigned32 = Unsigned32()

        val nextOut: Pointer = Pointer()
        val availOut: Unsigned32 = Unsigned32()
        val totalOutLo32: Unsigned32 = Unsigned32()
        val totalOutHi32: Unsigned32 = Unsigned32()

        val state: Pointer = Pointer()

        val alloc: Pointer = Pointer()
        val free: Pointer = Pointer()
        val opaque: Pointer = Pointer()
    }

    fun BZ2_bzCompressInit(@Direct stream: BzStream, blockSize100k: Int, verbosity: Int, workFactor: Int): Int
    fun BZ2_bzCompress(stream: BzStream, action: Int): Int
    fun BZ2_bzCompressEnd(stream: BzStream): Int

    fun BZ2_bzDecompressInit(@Direct stream: BzStream, blockSize100k: Int, verbosity: Int, small: Int): Int
    fun BZ2_bzDecompress(stream: BzStream): Int
    fun BZ2_bzDecompressEnd(stream: BzStream): Int

    companion object {
        const val BZ_RUN: Int = 0
        const val BZ_FLUSH: Int = 1
        const val BZ_FINISH: Int = 2

        const val BZ_OK: Int = 0
        const val BZ_RUN_OK: Int = 1
        const val BZ_FLUSH_OK: Int = 2
        const val BZ_FINISH_OK: Int = 3
        const val BZ_STREAM_END: Int = 4
        const val BZ_SEQUENCE_ERROR: Int = -1
        const val BZ_PARAM_ERROR: Int = -2
        const val BZ_MEM_ERROR: Int = -3
        const val BZ_DATA_ERROR: Int = -4
        const val BZ_DATA_ERROR_MAGIC: Int = -5
        const val BZ_IO_ERROR: Int = -6
        const val BZ_UNEXPECTED_EOF: Int = -7
        const val BZ_OUTBUFF_FULL: Int = -8
        const val BZ_CONFIG_ERROR: Int = -9

        fun load(): LibBzip2 {
            return LibraryLoader.loadLibrary(
                LibBzip2::class.java,
                mapOf(
                    LibraryOption.LoadNow to true
                ),
                "bz2"
            )
        }
    }
}
