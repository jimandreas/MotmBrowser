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

package com.bammellab.motm.pdb

import android.widget.TextView
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    fun pdbFetcherCoroutine() = runBlocking {

        val job = launch(IO) {
            //loadTextViaRetrofit()
            getPdbInfo(pdbid)
        }
        //job.join()
    }


    data class PDBdata(
            val pdbx_descriptor: String = "",
            val title: String = ""
    )

    data class PDBstruct(
            val struct: PDBdata
    )


    private val client = OkHttpClient()

    fun getPdbInfo(pdb: String) {
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
                        setText(thingie.struct.title)
                    }
                }
            }
        })
    }
    fun setText(text: String) = runBlocking {
        val job = launch(Main) {
            pdbTextView.text = text
        }
    }

}

    /*val request = Request.Builder()
            .url("https://data.rcsb.org/rest/v1/core/entry/$pdb")
            .build()

    try {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val body = response.body?.string()
            //println(body)
            val gson = GsonBuilder().create()
            val thingie = gson.fromJson(body!!, PDBstruct::class.java)

            if (thingie != null) {
                pdbTextView.text = thingie.struct.title
            }
            //return thingie.struct
        }
    } catch (e: Exception) {
        Timber.e(e, "OKHTTP Failure on $pdbid")
    }*/


/*

        private fun loadTextViaRetrofit() {
//        val retrofit = Retrofit.Builder()
//                .baseUrl("https://www.rcsb.org/pdb/rest/")
//                .addConverterFactory(SimpleXmlConverterFactory.create())
//                .build()
        val pdbApi = URLs.retrofit.create(PdbApi::class.java)
        val pdbRequest = pdbApi.getPdbInfo(pdbid)
        pdbRequest.enqueue(this)
    }

    override fun onResponse(call: Call<Pdb>, response: Response<Pdb>) {
        Timber.d("Success!")
        val entries = response.body()!!.entries
        if (entries.size > 0) {
            pdbTextView.text = entries[0].title
        }
    }

    override fun onFailure(call: Call<Pdb>, throwable: Throwable) {
        Timber.e(throwable, "Error on PDBs: ")
    }
*/

