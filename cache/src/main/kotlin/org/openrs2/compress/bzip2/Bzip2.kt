package org.openrs2.compress.bzip2

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.SequenceInputStream

object Bzip2 {
    private val library: LibBzip2? = try {
        LibBzip2.load()
    } catch (ex: Throwable) {
        ex.printStackTrace()
        null
    }

    fun createHeaderlessInputStream(input: InputStream): InputStream {
        return BZip2CompressorInputStream(SequenceInputStream(ByteArrayInputStream("BZh1".toByteArray()), input))
    }

    fun createHeaderlessOutputStream(output: OutputStream): OutputStream {
        val stream = object : OutputStream() {
            var skip = 4
            override fun write(b: Int) {
                if (skip == 0) {
                    output.write(b)
                } else {
                    skip--
                }
            }

            override fun flush() {
                output.flush()
            }

            override fun close() {
                output.close()
            }
        }
        return if (library != null) {
            Bzip2OutputStream(library, stream, 1)
        } else {
            BZip2CompressorOutputStream(stream, 1)
        }
    }
}
