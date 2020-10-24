/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bammellab.mollib.disklrucache

import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.Reader
import java.io.StringWriter
import java.nio.charset.Charset

/** Junk drawer of utility methods.  */
internal object LruCacheUtil {

    val US_ASCII: Charset = Charset.forName("US-ASCII")
    val UTF_8: Charset = Charset.forName("UTF-8")

    @Throws(IOException::class)
    fun readFully(readerIn: Reader): String {
        readerIn.use { reader ->
            val writer = StringWriter()
            val buffer = CharArray(1024)
            var count: Int = reader.read(buffer)
            while (count != -1) {
                writer.write(buffer, 0, count)
                count = reader.read(buffer)
            }
            return writer.toString()
        }
    }

    /**
     * Deletes the contents of `dir`. Throws an IOException if any file
     * could not be deleted, or if `dir` is not a readable directory.
     */
    @Throws(IOException::class)
    fun deleteContents(dir: File) {
        val files = dir.listFiles() ?: throw IOException("not a readable directory: $dir")
        for (file in files) {
            if (file.isDirectory) {
                deleteContents(file)
            }
            if (!file.delete()) {
                throw IOException("failed to delete file: $file")
            }
        }
    }

    fun closeQuietly(/*Auto*/closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (rethrown: RuntimeException) {
                throw rethrown
            } catch (ignored: Exception) {
            }

        }
    }


}
