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

@file:Suppress("UNUSED_VARIABLE")

package com.bammellab.motm.pdb

import android.widget.TextView
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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


    data class PDBdata(
            val pdbx_descriptor: String = "",
            val title: String = ""
    )

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

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val body = response.body?.string()
                        //println(body)
                        val gson = GsonBuilder().create()
                        val thingie = gson.fromJson(body!!, PDBstruct::class.java)

                        if (thingie != null) {
                            setTextCoroutine(thingie.struct.title)
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