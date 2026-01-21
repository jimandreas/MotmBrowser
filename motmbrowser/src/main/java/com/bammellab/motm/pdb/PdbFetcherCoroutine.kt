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

@file:Suppress("UNUSED_VARIABLE", "PropertyName")

package com.bammellab.motm.pdb

import android.annotation.SuppressLint
import android.util.Log
import android.widget.TextView
import androidx.annotation.Keep
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.*
import okio.IOException


/**
 * Query the RCSB.org database for the title of the given PDB id.
 * Now uses OkHttp3 running on an IO thread using Kotlin Co-routines.
 */
class PdbFetcherCoroutine(
        private val pdbid: String,
        private val pdbTextView: TextView
) {

    private val scope = MainScope()
    fun pdbFetcherCoroutine() = scope.launch {

        getPdbInfo(pdbid)

    }


    @Keep // Prevents R8 from eating this class
    data class PDBdata(
            val pdbx_descriptor: String = "",
            val title: String = ""
    )

    @Keep // Prevents R8 from eating this class
    data class PDBstruct(
            val struct: PDBdata
    )

    private val client = OkHttpClient()

    private suspend fun getPdbInfo(pdb: String) {
        val data = withContext(IO) {
            val request = Request.Builder()
                    .url("https://data.rcsb.org/rest/v1/core/entry/$pdb")
                    .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                @SuppressLint("LogNotTimber")
                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        try {
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")

                            val body = response.body?.string()
                            //println(body)
                            val gson = GsonBuilder().create()
                            val result = gson.fromJson(body!!, PDBstruct::class.java)

                            if (result == null) {
                                setTextCoroutine("Failed Description Download.  Please report.")
                            } else {
                                setTextCoroutine(result.struct.title)
                            }

                        } catch (e: Exception) {
                            Log.e("getPdbInfo", "DOWNLOAD INFO ERROR", e)
                        }
                    }
                }
            })
        }
    }

    // there is probably a better way to do this...
    fun setTextCoroutine(text: String) = scope.launch {
        setText(text)
    }

    private suspend fun setText(text: String) = withContext(Dispatchers.Main) {
        pdbTextView.text = text
    }
}