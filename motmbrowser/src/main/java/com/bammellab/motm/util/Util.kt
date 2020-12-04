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

@file:Suppress("LiftReturnOrAssignment", "unused")

package com.bammellab.motm.util

import android.content.Context
import android.os.StrictMode
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bammellab.motm.BuildConfig

object Util {

    // copy / paste from wikipedia DeviceUtil:

    fun hideSoftKeyboard(view: View) {
        val keyboard = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // Not using getCurrentFocus as that sometimes is null, but the keyboard is still up.
        keyboard.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun permitDiskReads(func: () -> Any?) : Any? {
        if (BuildConfig.DEBUG) {
            val oldThreadPolicy = StrictMode.getThreadPolicy()
            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder(oldThreadPolicy)
                            .permitDiskReads().build())
            val anyValue = func()
            StrictMode.setThreadPolicy(oldThreadPolicy)

            return anyValue
        } else {
            return func()
        }
    }

}