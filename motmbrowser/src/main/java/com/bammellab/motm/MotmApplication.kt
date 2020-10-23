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

@file:Suppress("unused", "unused_variable", "unused_parameter", "deprecation")

package com.bammellab.motm

import android.app.Application
import android.content.Context
import com.bammellab.motm.data.MotmCategories
import com.bammellab.motm.data.PDBs
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber

class MotmApplication : Application() {

    //lateinit var retrofit: Retrofit
    var context: Context = this
    val okHttpClient = OkHttpClient()
    var saveSelectedTabNumber = 0

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
//        retrofit = Retrofit.Builder()
//                .baseUrl("https://www.rcsb.org/pdb/rest/")
//                .addConverterFactory(SimpleXmlConverterFactory.create())
//                .build()
    }
}