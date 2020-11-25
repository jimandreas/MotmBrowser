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
import android.widget.Toast
import com.bammellab.mollib.common.util.ConnectionUtil
import com.bammellab.mollib.pdbDownload.MollibDefs.RCSB_DOWNLOAD_PATH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

    // https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md#structured-concurrency-lifecycle-and-coroutine-parent-child-hierarchy
    suspend fun downloadPdb(pdbid: String) {
        val data = withContext(Dispatchers.IO) {
            downloadPdbBackground(pdbid)
        }
    }

    private fun checkCacheForPdb(pdbid: String): Boolean {

        try {
            val cacheDir = activity.externalCacheDir
            makeDirIfNotThere(cacheDir!!)
            val file = File(cacheDir, "PDB/$pdbid.pdb")
            if (file.exists()) {
                Timber.v("file already exists, reading")
                val inputStream = FileInputStream(file)
                pdbCallback!!.loadPdbFromStream(inputStream)
                return true
            }
        } catch (e: Exception) {
            Timber.e("IO error")
        }
        return false
    }


    /**
     *
     * Attempt to download the gzip'ed pdb file from the
     * RCSB.org service.   Save it as an ungzip'ed file in the filesystem
     *
     * @param pdbid - the PDB file identifier
     */

    private fun downloadPdbBackground(pdbid: String) {

        if (checkCacheForPdb(pdbid)) {
            return
        }
        Timber.v("downloadPdbBackground: pdbid $pdbid")

        var inputStream: InputStream?

        if (!connectionUtil.isOnline()) {
            activity.runOnUiThread {
                Toast.makeText(activity, "No networking available, sorry", Toast.LENGTH_LONG)
                        .show()
            }
            return
        }

        val url = (RCSB_DOWNLOAD_PATH
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
     * this routine reads in the pdb file
     * in 10K chunks and writes it to a temp "xyxxy"
     * file.
     * When complete it renames the "xyzzy" file to the
     * real PDB.   This avoids partial reads looking like
     * valid files.
     */
    private fun downloadPdbFromHttp(downloadInputStream: InputStream?,
                                    pdbid: String) {
        var inputStreamAfterDownload: InputStream? = null

        try {
            val cacheDir = activity.externalCacheDir
            makeDirIfNotThere(cacheDir!!)
            val realFile = File(cacheDir, "PDB/$pdbid.pdb")
            val file = File(cacheDir, "PDB/xyzzy")

            val fileOutputStream = FileOutputStream(file)

            val dataInputStream = DataInputStream(downloadInputStream!!)
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
            fileOutputStream.close()
            file.renameTo(realFile)

            inputStreamAfterDownload = FileInputStream(realFile)


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

    private fun makeDirIfNotThere(filePath: File) {
        try {
            val dir = File(filePath, "PDB")
            dir.mkdir()
        } catch (e: Exception) {
            Timber.e(e, "Failure on PDB dir creation")
        }
    }
}
