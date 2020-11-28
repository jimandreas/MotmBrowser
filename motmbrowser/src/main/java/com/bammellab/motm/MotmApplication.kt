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


@file:Suppress("unused")

package com.bammellab.motm

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.bammellab.motm.util.PrefsUtil
import okhttp3.OkHttpClient
import timber.log.Timber

class MotmApplication : Application() {
    var context: Context = this
    val okHttpClient = OkHttpClient()
    var saveSelectedTabNumber = 0

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        PrefsUtil.prefsContext = context


        /*
         * this is added as suggested here:
         * https://stackoverflow.com/a/57772287
         * to try to track down strict mode Google Play reporting.
         */
//        try {
//            if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
//                StrictMode.setThreadPolicy(
//                    StrictMode.ThreadPolicy.Builder()
//                        .detectAll()
//                        .penaltyLog()
//                        .build()
//                )
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//                    StrictMode.setVmPolicy(
//                        StrictMode.VmPolicy.Builder()
//                            .detectNonSdkApiUsage()
//                            .penaltyLog()
//                            .build()
//                    )
//                }
//            }
//        } catch (e: Exception) {
//            Timber.e(e,"Fail on StrictMode setup")
//        }
    }
}