/*
 * Copyright (C) 2016-2018 James Andreas
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
 * limitations under the License
 */

@file:Suppress("deprecation")
package com.bammellab.motm.pdb

import android.widget.TextView
import com.bammellab.motm.data.URLs
import com.bammellab.motm.data.URLs.RCSB_PDB_INFO_PREFIX
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber


class PdbFetcherCoroutine(
        private val pdbid: String,
        private val pdbTextView: TextView
) : Callback<Pdb> {

    fun pdbFetcherCoroutine() = runBlocking {

        val job = launch {
            loadTextViaRetrofit()
        }
        job.join()
    }

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

}