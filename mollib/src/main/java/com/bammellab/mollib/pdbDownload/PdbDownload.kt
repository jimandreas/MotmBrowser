/*
 *  Copyright 2020 Bammellab / James Andreas
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

package com.bammellab.mollib.pdbDownload

import android.app.Activity
import android.os.Environment
import android.widget.Toast
import com.bammellab.mollib.common.util.ConnectionUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import timber.log.Timber
import java.io.*
import java.util.zip.GZIPInputStream

interface PdbCallback {
    fun loadPdbFromStream(stream: InputStream)
}

class PdbDownload(private val activity: Activity) {
    private val connectionUtil = ConnectionUtil(activity)
    private val client: OkHttpClient
    private var pdbCallback: PdbCallback? = null

    init {
        val okBuilder = OkHttpClient.Builder()
        client = okBuilder.build()
    }

    fun initPdbCallback(cb: PdbCallback) {
        pdbCallback = cb
    }

    fun downloadPdb(pdbid: String) = runBlocking {
        launch(Dispatchers.IO) {
            downloadPdbBackground(pdbid)
        }
    }


    /**
     *
     * Attempt to download the gzip'ed pdb file from the
     * RCSB.org service.   Save it as an ungzip'ed file in the filesystem
     *
     * @param pdbid - the PDB file identifier
     */

    private fun downloadPdbBackground(pdbid: String) {

        Timber.v("downloadPdbBackground: pdbid $pdbid")

        var inputStream: InputStream?

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
            Timber.v("downloadPdbBackground: download from net: pdbid $pdbid")
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Timber.e(e, "downloadPdbBackground: Failed to load %s", call.request().url)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful)
                        throw IOException("Unexpected code $response")

                    // val responseHeaders = response.headers()
                    //                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    //                        Timber.i(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    //                    }

                    Timber.i("downloading the PDB in GZIP form: pdbid $pdbid")
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
        var inputStreamAfterDownload: InputStream? = null

        try {
            val dataInputStream = DataInputStream(downloadInputStream!!)
            val myFile = getDiskCacheDir(pdbid)
            val fileOutputStream = FileOutputStream(myFile)

            val dataOutputStream = DataOutputStream(fileOutputStream)

            val buffer = ByteArray(10240)
            var length = dataInputStream.read(buffer)
            while (length > 0) {
                dataOutputStream.write(buffer, 0, length)
                length = dataInputStream.read(buffer)
            }
            dataInputStream.close()
            dataOutputStream.close()
            downloadInputStream.close()

            inputStreamAfterDownload = FileInputStream(getDiskCacheDir(pdbid))


        } catch (e: IOException) {
            Timber.e(e, "downloadPdbFromHttp: error on download")

        } finally {

            if (inputStreamAfterDownload != null) {
                if (pdbCallback != null) {
                    Timber.v("download finished - call loadPdbFromStream: pdbid $pdbid")
                    pdbCallback!!.loadPdbFromStream(inputStreamAfterDownload)
                } else {
                    Timber.e("downloadPdbBackgroun: Error callback not set")
                }
                // cacheStream.close()  NOTE: is closed later in reader thread, do not close here!!
            } else {
                Timber.e("downloadPdbFromHttp: unhandled error on download")
            }
        }
    }
}
