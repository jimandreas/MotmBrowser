/*
 *  Copyright 2020 James Andreas
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

@file:Suppress("unused", "unused_variable", "unused_parameter", "deprecation")

package com.bammellab.mollib.disklrucache


import android.app.Activity
import android.os.Environment
import android.widget.Toast
import com.bammellab.mollib.common.util.ConnectionUtil
import okhttp3.*
import timber.log.Timber
import java.io.*
import java.util.zip.GZIPInputStream

interface PdbCallback {
    fun loadPdbFromStream(stream: InputStream)
}

class PdbCache(private val activity: Activity) {
    private var lruCacheDisk: LruCacheDisk? = null
    private val connectionUtil = ConnectionUtil(activity)
    private val client: OkHttpClient
    private var pdbCallback : PdbCallback? = null

    init {
        val okBuilder = OkHttpClient.Builder()
        client = okBuilder.build()
        initDiskCache()
    }

    fun initPdbCallback(cb: PdbCallback) {
        pdbCallback = cb
    }

    fun downloadPdb(pdbid: String) {
        Thread { downloadPdbBackground(pdbid) }.start()
    }

    /**
     * First query the LruCache - and process the (ungzipped) stream from the cache
     * if available.
     *
     * Otherwise, attempt to download the gzip'ed pdb file from the
     * RCSB.org service.   Save it as an ungzip'ed file in the cache,
     * and process it from the cache.
     *
     * @param pdbid - the PDB file identifier
     */
    private fun downloadPdbBackground(pdbid: String) {

        var inputStream = queryLruCache(pdbid)
        if (inputStream != null) {
            if (pdbCallback != null) {
                pdbCallback!!.loadPdbFromStream(inputStream)
            } else {
                Timber.e("downloadPdbBackgroun: Error callback not set")
            }

            return
        }

        if (!connectionUtil.isOnline()) {
            activity.runOnUiThread {
                Toast.makeText(activity, "No networking available, sorry", Toast.LENGTH_LONG)
                        .show()
            }
            return
        }
        // TODO: pull out this hardwired URL
        val url = ("https://files.rcsb.org/download/"
                + pdbid
                + ".pdb.gz")
        val request = Request.Builder()
                .url(url)
                .build()
        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    //Timber.e(e, "Failed to load %s", call.request().url)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful)
                        throw IOException("Unexpected code $response")

                    // val responseHeaders = response.headers()
                    //                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    //                        Timber.i(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    //                    }

                    Timber.i("loaded the PDB!")

                    // inputStream = GZIPInputStream(response.body()!!.byteStream())
                    val responseBody = response.body
                    inputStream = GZIPInputStream(responseBody!!.byteStream())
                    downloadPdbFromHttp(inputStream, pdbid)
                    response.close()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Timber.d("end of downloadPdb processing")
        }
    }

    /**
     * see also:
     * https://github.com/square/okhttp/wiki/Interceptors
     * and
     * http://stackoverflow.com/a/37848518/3853712
     * Status: non operational
     */
    private inner class CachingControlInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            val originalResponse = chain.proceed(request)
            return originalResponse.newBuilder()
                    //                    .header("Cache-Control", (Utility.isNetworkAvailable()) ?
                    //                            "public, max-age=60" :  "public, max-stale=604800")
                    .header("Cache-Control", if (connectionUtil.isOnline())
                        "public, max-age=60"
                    else
                        "public, only-if-cached, max-stale=604800")
                    .build()
        }
    }

    /**
     * Initializes the disk cache.  Note that this includes disk access so this should not be
     * executed on the main/UI thread. By default an PdbCache does not initialize the disk
     * cache when it is created, instead you should call initDiskCache() to initialize it on a
     * background thread.
     */
    private fun initDiskCache() {

        // Set up disk cache

        val diskCacheDir = getDiskCacheDir(CACHE_DIR)
        Timber.d("initDiskCache: entering, diskCacheDir is %s", diskCacheDir)

        Thread {
            if (!diskCacheDir.exists()) {
                diskCacheDir.mkdirs()
            }
            try {
                lruCacheDisk = LruCacheDisk.open(
                        diskCacheDir, 1, 1, CACHE_SIZE.toLong())
                if (lruCacheDisk == null) {
                    Timber.e("error on DiskLruCache.open() call")
                } else {
                    Timber.d("diskLruCache set, dir is %s", diskCacheDir)
                }
            } catch (e: IOException) {
                lruCacheDisk = null
                Timber.e(e, "failed disk cache open")
            } finally {
                Timber.d("exiting diskLruCache setup block")
            }
        }.start()
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * MODIFIED: to remove "external storage" permission from manifest.
     * The permission is not needed on API 19+ for app specific
     * external storage.
     * http://youtu.be/C28pvd2plBA?t=8m11s
     *
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    @Suppress("SameParameterValue")
    private fun getDiskCacheDir(uniqueName: String): File {

        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir

        val state = Environment.getExternalStorageState()
        val cachePath: String
        val fullPath: String

        cachePath = if (Environment.MEDIA_MOUNTED == state || !Environment.isExternalStorageRemovable()) {

            // File cacheDir = activity.getExternalCacheDir();
            val cacheDir = activity.getExternalFilesDir(null)
            if (cacheDir != null) {
                activity.getExternalFilesDir(null)!!.path
            } else {
                activity.filesDir.path
            }
        } else {
            activity.cacheDir.path
        }

        fullPath = cachePath + File.separator + uniqueName
        return File(fullPath)
    }

    private fun downloadPdbFromHttp(downloadInputStream: InputStream?,
                                    pdbid: String) {
        var cacheStream: InputStream? = null
        if (lruCacheDisk == null) {
            Timber.e("null cache - error on setup")
            return
        }
        val editor: LruCacheDisk.Editor
        try {
            editor = lruCacheDisk!!.edit(pdbid)
        } catch (e: IOException) {
            Timber.e("unhandled error on diskLruCache edit of %s", pdbid)
            return
        }

        try {
            val dataInputStream = DataInputStream(downloadInputStream!!)
            val outputStream = editor.newOutputStream(VALUE_IDX)
            val dataOutputStream = DataOutputStream(outputStream)

            val buffer = ByteArray(10240)
            var length = dataInputStream.read(buffer)
            while (length > 0) {
                dataOutputStream.write(buffer, 0, length)
                length = dataInputStream.read(buffer)
            }
            dataInputStream.close()
            dataOutputStream.close()
            downloadInputStream.close()
            outputStream.close()
            editor.commit()
            // OK the PDB should be in the cache now
            cacheStream = queryLruCache(pdbid)

        } catch (e: IOException) {
            Timber.e(e, "IO Exception on lru cache download")
            try {
                editor.abort()
            } catch (ignored: IOException) {
            }

        } finally {
            if (cacheStream != null) {
                if (pdbCallback != null) {
                    pdbCallback!!.loadPdbFromStream(cacheStream)
                } else {
                    Timber.e("downloadPdbBackgroun: Error callback not set")
                }
                // cacheStream.close()  NOTE: is closed later in reader thread, do not close here!!
            } else {
                Timber.e("unhandled error on LRU caching")
            }
        }
    }

    private fun queryLruCache(pdbid: String): InputStream? {

        if (lruCacheDisk == null) {
            return null
        }
        try {
            val snapshot = lruCacheDisk!!.get(pdbid) ?: return null
            return snapshot.getInputStream(VALUE_IDX)
        } catch (e: IOException) {
            Timber.e(e, "IO exception on LRU cache query")
        }

        return null
    }

    companion object {
        private const val CACHE_SIZE = 10 * 1024 * 1024
        private const val CACHE_DIR = "pdbFileCache"
        private const val VALUE_IDX = 0  // which LRU cache meta const value to use
    }
}
